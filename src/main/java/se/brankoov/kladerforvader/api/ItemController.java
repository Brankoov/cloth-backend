package se.brankoov.kladerforvader.api;

import org.springframework.web.bind.annotation.*;
import se.brankoov.kladerforvader.domain.ItemState;
import se.brankoov.kladerforvader.domain.Location;
import se.brankoov.kladerforvader.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {
    private final ItemService svc;

    public ItemController(ItemService svc) {
        this.svc = svc;
    }

    @GetMapping
    public List<ItemState> all(@RequestParam String userId) {
        return svc.getStates(userId);
    }

    public record MoveDto(String userId, String itemId, Location location) {}

    @PostMapping("/move")
    public void move(@RequestBody MoveDto dto) {
        svc.move(dto.userId(), dto.itemId(), dto.location());
    }
}
