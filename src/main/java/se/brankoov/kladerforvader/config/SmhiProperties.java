// src/main/java/.../config/SmhiProperties.java
package se.brankoov.kladerforvader.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "smhi")
public record SmhiProperties(
        String baseUrl,        // t.ex. https://opendata-download-metfcst.smhi.se
        String category,       // pmp3g
        int version,           // 2
        long ttlMinutes,        //45
        String forecastPath // <-- NY PROPERTY

) {}
