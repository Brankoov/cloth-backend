package se.brankoov.kladerforvader.api;


import org.springframework.web.bind.annotation.*;
import se.brankoov.kladerforvader.service.WeatherService;

import java.util.List;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api/weather")
@Slf4j
public class WeatherController {

    private final WeatherService svc;

    public WeatherController(WeatherService svc) {
        this.svc = svc;
    }

    @GetMapping("/daily")
    public List<WeatherService.DayWeather> daily(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam int days
    ) {
        log.info("GET /api/weather/daily lat={}, lon={}, days={}", lat, lon, days);
        return svc.daily(lat, lon, days);
    }
}
