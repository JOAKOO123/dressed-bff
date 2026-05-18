package cl.dressed.bff.client;

import cl.dressed.bff.dto.catalog.GarmentResponseDTO;
import cl.dressed.bff.exception.BffException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

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
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/api/catalog/products")
                .queryParam("page", page)
                .queryParam("pageSize", pageSize);

        if (category != null && !category.isBlank()) {
            builder.queryParam("category", category);
        }
        if (size != null && !size.isBlank()) {
            builder.queryParam("size", size);
        }
        if (inStock != null) {
            builder.queryParam("inStock", inStock);
        }
        if (sort != null && !sort.isBlank()) {
            builder.queryParam("sort", sort);
        }

        String uri = builder.build().toUriString();

        return backendClient.get()
                .uri(uri)
                .retrieve()
                .onStatus(HttpStatus.BAD_REQUEST::equals, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new BffException("Parámetros inválidos", HttpStatus.BAD_REQUEST)))
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();
    }

    public GarmentResponseDTO getProductById(Integer id) {
        return backendClient.get()
                .uri("/api/catalog/products/{id}", id)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new BffException("Prenda no encontrada", HttpStatus.NOT_FOUND)))
                .bodyToMono(GarmentResponseDTO.class)
                .block();
    }
}