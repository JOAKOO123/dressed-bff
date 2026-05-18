package cl.dressed.bff.service;

import cl.dressed.bff.client.CatalogClient;
import cl.dressed.bff.dto.catalog.GarmentResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogService {

    private final CatalogClient catalogClient;

    public Map<String, Object> getProducts(
            String category,
            String size,
            Boolean inStock,
            int page,
            int pageSize,
            String sort
    ) {
        log.info("Obteniendo productos del catálogo");
        return catalogClient.getProducts(category, size, inStock, page, pageSize, sort);
    }

    public GarmentResponseDTO getProductById(Integer id) {
        log.info("Obteniendo producto id={}", id);
        return catalogClient.getProductById(id);
    }
}