package cl.dressed.bff.client;

import cl.dressed.bff.exception.BffException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutfitClient {

    private final WebClient backendClient;

    public Map<String, Object> generateOutfit(String token) {
        log.info("Solicitando generación de outfit al backend");

        return backendClient.post()
                .uri("/api/outfits/generate")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, response -> response.bodyToMono(String.class)
                        .map(message -> new BffException(message, HttpStatus.BAD_REQUEST)))
                .onStatus(HttpStatus.UNAUTHORIZED::equals, response -> response.bodyToMono(String.class)
                        .map(message -> new BffException("No autenticado", HttpStatus.UNAUTHORIZED)))
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, response -> response.bodyToMono(String.class)
                        .map(message -> new BffException("Error interno del servidor", HttpStatus.BAD_GATEWAY)))
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    public List<Map<String, Object>> getMyOutfits(String token) {
        log.info("Solicitando outfits del usuario al backend");

        return backendClient.get()
                .uri("/api/outfits")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals, response -> response.bodyToMono(String.class)
                        .map(message -> new BffException("No autenticado", HttpStatus.UNAUTHORIZED)))
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, response -> response.bodyToMono(String.class)
                        .map(message -> new BffException("Error interno del servidor", HttpStatus.BAD_GATEWAY)))
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .block();
    }
}