package cl.dressed.bff.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(

        @NotBlank(message = "email es requerido")
        @Email(message = "formato de email inválido")
        @Size(max = 255, message = "email máximo 255 caracteres")
        String email,

        @NotBlank(message = "password es requerido")
        @Size(min = 8, max = 72, message = "password debe tener entre 8 y 72 caracteres")
        String password

) {}
