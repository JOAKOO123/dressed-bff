package cl.dressed.bff.service;

import cl.dressed.bff.client.CatalogClient;
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
        log.info("Obteniendo productos - page={} pageSize={} category={} size={} inStock={}",
            page, pageSize, category, size, inStock);
        return catalogClient.getProducts(category, size, inStock, page, pageSize, sort);
    }
}