package cl.dressed.bff.dto.auth;

public record LoginResponseDTO(
        Long id,
        String email,
        Boolean active,
        String token
) {}
