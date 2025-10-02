package se.brankoov.kladerforvader.smhi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SnowParameter(String name, List<Double> values) {}
