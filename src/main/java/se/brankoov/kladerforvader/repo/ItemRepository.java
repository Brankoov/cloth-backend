package se.brankoov.kladerforvader.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import se.brankoov.kladerforvader.domain.Item;

public interface ItemRepository extends JpaRepository<Item, String> {}
