package cl.dressed.bff.dto.auth;

import java.time.LocalDateTime;

public record RegisterResponseDTO(
        Long id,
        String email,
        Boolean active,
        LocalDateTime createdAt,
        String token
) {}
