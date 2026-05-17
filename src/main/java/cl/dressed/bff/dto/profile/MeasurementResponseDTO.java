package cl.dressed.bff.dto.profile;

import java.math.BigDecimal;

public record MeasurementResponseDTO(
        BigDecimal heightCm,
        BigDecimal shouldersCm,
        BigDecimal chestCm,
        BigDecimal waistCm,
        BigDecimal hipsCm,
        BigDecimal torsoLengthCm,
        BigDecimal legLengthCm
) {}
