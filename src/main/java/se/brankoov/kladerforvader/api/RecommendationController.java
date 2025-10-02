package se.brankoov.kladerforvader.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.brankoov.kladerforvader.service.RecommendationService;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService svc;

    public RecommendationController(RecommendationService svc) {
        this.svc = svc;
    }

    // Exponerar den fullständiga rekommendationslistan
    @GetMapping
    public List<DailyRecommendation> getRecommendations(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam int days
    ) {
        // Observera: userId används inte just nu men kommer behövas senare
        // för att hämta användarens sparade adress/inställningar.
        // Tills dess kör vi direkt på lat/lon.
        return svc.recommendations(lat, lon, days);
    }
}