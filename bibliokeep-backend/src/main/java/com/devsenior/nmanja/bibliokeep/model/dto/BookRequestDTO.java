package com.devsenior.nmanja.bibliokeep.model.dto;

import java.util.List;

import com.devsenior.nmanja.bibliokeep.model.entity.BookStatus;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BookRequestDTO(
        @NotBlank(message = "El ISBN es requerido")
        @Pattern(regexp = "^(\\d{10}|\\d{13})$", message = "El ISBN debe tener 10 o 13 dígitos")
        String isbn,

        @NotBlank(message = "El título es requerido")
        @Size(max = 500, message = "El título no debe exceder 500 caracteres")
        String title,

        @NotEmpty(message = "Se requiere al menos un autor")
        @NotBlank(message = "El nombre del autor no puede estar vacío")
        List<String> authors,

        String description,

        @Size(max = 1000, message = "La URL de la imagen no debe exceder 1000 caracteres")
        String thumbnail,

        BookStatus status,

        @Min(value = 1, message = "La calificación debe ser al menos 1")
        @Max(value = 5, message = "La calificación debe ser como máximo 5")
        Integer rating
) {
}
