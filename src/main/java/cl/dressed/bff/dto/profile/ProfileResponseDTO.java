package cl.dressed.bff.dto.profile;

import java.time.LocalDate;

public record ProfileResponseDTO(
        Long id,
        Long userId,
        String name,
        LocalDate birthDate,
        Integer age,
        String gender,
        String skinTone,
        String colorPalette
) {}
