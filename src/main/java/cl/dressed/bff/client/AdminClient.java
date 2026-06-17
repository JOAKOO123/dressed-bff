package cl.dressed.bff.client;

import cl.dressed.bff.exception.BffException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminClient {

    private final WebClient backendClient;

    public Map<String, Object> getMetrics(String token) {
        log.info("Llamando backend admin: /api/admin/metrics");
        Map<String, Object> response = backendClient.get()
                .uri("/api/admin/metrics")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatus.FORBIDDEN::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("Acceso denegado", HttpStatus.FORBIDDEN)))
                .onStatus(HttpStatus.UNAUTHORIZED::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("No autenticado", HttpStatus.UNAUTHORIZED)))
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("Error interno", HttpStatus.BAD_GATEWAY)))
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        log.info("Respuesta backend admin metrics: {}", response);
        return response;
    }

    public Map<String, Object> getUsers(String token, int page, int size) {
        String uri = "/api/admin/users?page=" + page + "&size=" + size;
        log.info("Llamando backend admin: {}", uri);
        Map<String, Object> response = backendClient.get()
                .uri(uri)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatus.FORBIDDEN::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("Acceso denegado", HttpStatus.FORBIDDEN)))
                .onStatus(HttpStatus.UNAUTHORIZED::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("No autenticado", HttpStatus.UNAUTHORIZED)))
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("Error interno", HttpStatus.BAD_GATEWAY)))
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        log.info("Respuesta backend admin users: {}", response);
        return response;
    }
}
