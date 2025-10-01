package se.brankoov.kladerforvader.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Item {
    @Id
    private String id;     // "hat", "socks1", ...
    private String title;  // Visningsnamn (valfritt)
}
