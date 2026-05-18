package cl.dressed.bff.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(

        @NotBlank(message = "email es requerido")
        @Email(message = "formato de email inválido")
        String email,

        @NotBlank(message = "password es requerido")
        String password

) {}
