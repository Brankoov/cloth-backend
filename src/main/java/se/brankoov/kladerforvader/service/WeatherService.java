package se.brankoov.kladerforvader.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import se.brankoov.kladerforvader.config.SmhiProperties;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class WeatherService {

    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);

    public record DayWeather(String day, double maxTemp, String summary) {}

    private final WebClient smhiClient;
    private final SmhiProperties props;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Cache<String, List<DayWeather>> cache;

    public WeatherService(@Qualifier("smhi") WebClient smhiClient, SmhiProperties props) {
        this.smhiClient = smhiClient;
        this.props = props;
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(props.ttlMinutes(), TimeUnit.MINUTES)
                .maximumSize(500)
                .build();
    }

    /**
     * Hämtar max-temperatur per dag (yyyy-MM-dd) för angivet lat/lon, begränsat till 'days'.
     * Datakälla: SMHI pmp3g (version från properties).
     */
    public List<DayWeather> daily(double lat, double lon, int days) {
        String cacheKey = lat + "," + lon + ":" + days;
        List<DayWeather> cached = cache.getIfPresent(cacheKey);
        if (cached != null) return cached;

        // Säkerställ punkt som decimal oavsett OS-locale
        String lonString = String.format(Locale.ROOT, "%.6f", lon);
        String latString = String.format(Locale.ROOT, "%.6f", lat);

        // Bygg path från application.yml (forecast-path med {0}=cat, {1}=ver, {2}=lon, {3}=lat)
        String path = MessageFormat.format(
                props.forecastPath(),
                props.category(),
                props.version(),
                lonString,
                latString
        );

        // Slå ihop baseUrl + path robust (utan dubbla snedstreck)
        String url = joinUrl(props.baseUrl(), path);

        log.info("Calling SMHI URL: {}", url);

        String raw = smhiClient.get()
                .uri(url)
                .exchangeToMono(resp -> {
                    if (resp.statusCode().is2xxSuccessful()) {
                        return resp.bodyToMono(String.class);
                    }
                    return resp.bodyToMono(String.class).defaultIfEmpty("")
                            .flatMap(body -> {
                                String preview = body.length() > 800 ? body.substring(0, 800) + "..." : body;
                                log.error("SMHI non-2xx status={}, body={}", resp.statusCode(), preview);
                                return Mono.error(new IllegalStateException("SMHI HTTP " + resp.statusCode().value()));
                            });
                })
                .block();

        try {
            JsonNode root = mapper.readTree(raw);
            JsonNode ts = root.path("timeSeries");
            if (!ts.isArray()) {
                log.error("Unexpected SMHI payload (no timeSeries). Raw preview: {}",
                        raw == null ? "null" : raw.substring(0, Math.min(300, raw.length())));
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Failed to fetch/parse data from SMHI");
            }

            Map<String, Double> maxPerDay = new LinkedHashMap<>();

            for (JsonNode item : ts) {
                // tid kan heta validTime (pmp3g) eller time i andra feeds
                String t = item.path("validTime").asText(null);
                if (t == null) t = item.path("time").asText(null);
                if (t == null) continue;

                // yyyy-MM-dd
                String day = t.length() >= 10 ? t.substring(0, 10) : t;

                // temperatur: pmp3g => parameters[name="t"].values[0]
                Double temp = null;

                // Vissa feeds kan ha data.air_temperature
                JsonNode data = item.path("data");
                if (data.isObject() && data.has("air_temperature")) {
                    temp = data.path("air_temperature").asDouble();
                }

                // Standard pmp3g via parameters
                if (temp == null) {
                    for (JsonNode p : item.path("parameters")) {
                        if ("t".equalsIgnoreCase(p.path("name").asText())) {
                            JsonNode values = p.path("values");
                            if (values.isArray() && values.size() > 0) {
                                temp = values.get(0).asDouble();
                            }
                            break;
                        }
                    }
                }
                if (temp == null) continue;

                maxPerDay.merge(day, temp, Math::max);
            }

            List<DayWeather> out = maxPerDay.entrySet().stream()
                    .limit(days)
                    .map(e -> new DayWeather(e.getKey(), e.getValue(), "")) // summary kan fyllas senare
                    .toList();

            cache.put(cacheKey, out);
            return out;

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Parse error from SMHI", e);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Failed to fetch/parse data from SMHI");
        }
    }

    private static String joinUrl(String base, String path) {
        if (base == null) base = "";
        if (path == null) path = "";
        boolean baseEnds = base.endsWith("/");
        boolean pathStarts = path.startsWith("/");
        if (baseEnds && pathStarts) {
            return base + path.substring(1);
        } else if (!baseEnds && !pathStarts) {
            return base + "/" + path;
        } else {
            return base + path;
        }
    }
}
