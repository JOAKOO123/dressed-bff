package cl.dressed.bff.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequestDTO(

        @NotBlank(message = "token es requerido")
        String token,

        @NotBlank(message = "password es requerido")
        @Size(min = 8, max = 72, message = "password debe tener entre 8 y 72 caracteres")
        String newPassword

) {}
