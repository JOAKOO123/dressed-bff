package cl.dressed.bff.dto.catalog;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record GarmentResponseDTO(
        Integer id,
        Integer storeId,
        String name,
        BigDecimal price,
        String imageUrl,
        String productLink,
        String category,
        List<String> sizes,
        String mainColor,
        String fit,
        String style,
        Boolean inStock,
        LocalDateTime createdAt,
        LocalDateTime lastUpdated,
        Map<String, Object> attributes
) {
}