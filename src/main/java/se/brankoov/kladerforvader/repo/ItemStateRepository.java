package se.brankoov.kladerforvader.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import se.brankoov.kladerforvader.domain.ItemState;

import java.util.List;

public interface ItemStateRepository extends JpaRepository<ItemState, Long> {
    List<ItemState> findByUserId(String userId);
    ItemState findByUserIdAndItemId(String userId, String itemId);
}