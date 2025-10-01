package se.brankoov.kladerforvader.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Settings {
    @Id
    private String userId;
    private String address;
    private int forecastDays;
}