package se.brankoov.kladerforvader.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class HttpConfig {

    // (Builder-Bean brukar finnas via WebFlux starter, men detta gör det säkert)
    @Bean
    WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    @Qualifier("smhi")
    WebClient smhiClient(WebClient.Builder builder, SmhiProperties props) {
        return builder
                .baseUrl(props.baseUrl())
                .build();
    }
}
