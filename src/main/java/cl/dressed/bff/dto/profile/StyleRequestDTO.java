package cl.dressed.bff.dto.profile;

import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record StyleRequestDTO(

        @NotNull(message = "styles no puede ser null")
        Set<String> styles

) {}
