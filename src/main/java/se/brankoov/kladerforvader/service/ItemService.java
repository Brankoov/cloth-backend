package se.brankoov.kladerforvader.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.brankoov.kladerforvader.domain.ItemState;
import se.brankoov.kladerforvader.domain.Location;
import se.brankoov.kladerforvader.repo.ItemStateRepository;

import java.util.List;

@Service
public class ItemService {
    private final ItemStateRepository repo;

    public ItemService(ItemStateRepository repo) {
        this.repo = repo;
    }

    public List<ItemState> getStates(String userId) {
        return repo.findByUserId(userId);
    }

    @Transactional
    public void move(String userId, String itemId, Location location) {
        ItemState s = repo.findByUserIdAndItemId(userId, itemId);
        if (s == null) {
            s = ItemState.builder()
                    .userId(userId)
                    .itemId(itemId)
                    .location(location)
                    .build();
        } else {
            s.setLocation(location);
        }
        repo.save(s);
    }
}