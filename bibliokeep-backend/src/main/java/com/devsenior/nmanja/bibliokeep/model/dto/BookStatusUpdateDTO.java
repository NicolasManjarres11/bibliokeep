package com.devsenior.nmanja.bibliokeep.model.dto;

import com.devsenior.nmanja.bibliokeep.model.entity.BookStatus;
import jakarta.validation.constraints.NotNull;

public record BookStatusUpdateDTO(
        @NotNull(message = "El estado es requerido")
        BookStatus status
) {
}
