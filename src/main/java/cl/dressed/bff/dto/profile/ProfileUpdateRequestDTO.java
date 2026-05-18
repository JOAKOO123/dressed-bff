package cl.dressed.bff.dto.profile;

import java.time.LocalDate;

public record ProfileUpdateRequestDTO(
        String name,
        LocalDate birthDate,
        String gender
) {}
