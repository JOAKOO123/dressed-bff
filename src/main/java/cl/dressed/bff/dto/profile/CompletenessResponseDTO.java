package cl.dressed.bff.dto.profile;

import java.util.List;

public record CompletenessResponseDTO(
        int percentage,
        List<String> missing,
        String message
) {}
