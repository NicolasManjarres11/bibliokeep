package com.devsenior.nmanja.bibliokeep.model.dto;

import com.devsenior.nmanja.bibliokeep.model.entity.BookStatus;
import jakarta.validation.constraints.*;

import java.util.List;

public record BookRequestDTO(
        @NotBlank(message = "El ISBN es requerido")
        @Pattern(regexp = "^(\\d{10}|\\d{13})$", message = "El ISBN debe tener 10 o 13 dígitos")
        String isbn,

        @NotBlank(message = "El título es requerido")
        @Size(max = 500, message = "El título no debe exceder 500 caracteres")
        String title,

        @NotEmpty(message = "Se requiere al menos un autor")
        List<@NotBlank(message = "El nombre del autor no puede estar vacío") String> authors,

        String description,

        @Size(max = 1000, message = "La URL de la imagen no debe exceder 1000 caracteres")
        String thumbnail,

        BookStatus status,

        @Min(value = 1, message = "La calificación debe ser al menos 1")
        @Max(value = 5, message = "La calificación debe ser como máximo 5")
        Integer rating
) {
}
