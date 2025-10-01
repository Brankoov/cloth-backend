package se.brankoov.kladerforvader.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.brankoov.kladerforvader.domain.Item;
import se.brankoov.kladerforvader.repo.ItemRepository;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seedItems(ItemRepository items) {
        return args -> {
            // Lägg in de itemId du har i Android (strings.xml -> all_item_ids)
            String[] ids = new String[] {
                    "socks1","sock3","sock2","skor","shoes2","shirt1","set1",
                    "pokemon","pants3","pants2","kid1","hat","blackhoodie"
            };
            for (String id : ids) {
                if (!items.existsById(id)) {
                    items.save(Item.builder().id(id).title(id).build());
                }
            }
        };
    }
}
