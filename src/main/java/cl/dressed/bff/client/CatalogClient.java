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
public class CatalogClient {

    private final WebClient backendClient;

    public Map<String, Object> getProducts(
            String category,
            String size,
            Boolean inStock,
            int page,
            int pageSize,
            String sort
    ) {
        StringBuilder uri = new StringBuilder("/api/catalog/products?page=")
                .append(page)
                .append("&size=")
                .append(pageSize);

        if (category != null && !category.isBlank()) {
            uri.append("&category=").append(category);
        }
        if (inStock != null) {
            uri.append("&inStock=").append(inStock);
        }
        if (sort != null && !sort.isBlank()) {
            uri.append("&sort=").append(sort);
        }
        if (size != null && !size.isBlank()) {
            uri.append("&size=").append(size);
        }

        return backendClient.get()
                .uri(uri.toString())
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("Parámetros inválidos", HttpStatus.BAD_REQUEST)))
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, r -> r.bodyToMono(String.class)
                        .map(b -> new BffException("Error interno del servidor", HttpStatus.BAD_GATEWAY)))
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
}