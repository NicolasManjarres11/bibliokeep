package com.devsenior.nmanja.bibliokeep.model.dto;

import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Set;

public record UserRequestDTO(
        @NotBlank(message = "El correo electrónico es requerido")
        @Email(message = "El correo electrónico debe ser válido")
        @Size(max = 255, message = "El correo electrónico no debe exceder 255 caracteres")
        String email,

        @NotBlank(message = "La contraseña es requerida")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        String password,

        Set<String> preferences,

        @Min(value = 1, message = "La meta anual debe ser al menos 1")
        @Max(value = 1000, message = "La meta anual debe ser como máximo 1000")
        Integer annualGoal,


        List<String> roles
) {
}
