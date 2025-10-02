package se.brankoov.kladerforvader.service;

import org.springframework.stereotype.Service;
import se.brankoov.kladerforvader.api.DailyRecommendation;

import java.util.List;

@Service
public class RecommendationService {
    private final WeatherService weather;

    public RecommendationService(WeatherService weather) {
        this.weather = weather;
    }

    public List<DailyRecommendation> recommendations(double lat, double lon, int days) {
        return weather.daily(lat, lon, days).stream()
                .map(dw -> {
                    String temp = "Högst: " + Math.round(dw.maxTemp()) + "°";
                    String rec  = rule(dw.maxTemp());
                    return new DailyRecommendation(dw.day(), temp, dw.summary(), rec);
                })
                .toList();
    }


    private String rule(double t) {
        if (t >= 20) return "T-shirt & shorts. Keps vid sol.";
        if (t >= 14) return "Lätt tröja + byxor. Tunn jacka vid vind.";
        if (t >= 8)  return "Hoodie/jacka + långbyxor. Mössa på morgonen.";
        if (t >= 2)  return "Fodrad jacka. Tunna vantar.";
        return "Vinterkit: varm jacka, mössa, vantar, lager på lager.";
    }
}
