package se.brankoov.kladerforvader.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ItemState {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId; // Vi kör "demo" tills auth finns
    private String itemId; // Kopplar till Item.id

    @Enumerated(EnumType.STRING)
    private Location location;
}