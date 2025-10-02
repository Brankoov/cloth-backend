package se.brankoov.kladerforvader.smhi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SnowTimeSeries(
        String time,
        Map<String, Double> data
) {}
