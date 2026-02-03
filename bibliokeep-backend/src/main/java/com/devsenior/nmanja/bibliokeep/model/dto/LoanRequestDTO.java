package com.devsenior.nmanja.bibliokeep.model.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record LoanRequestDTO(
        @NotNull(message = "El ID del libro es requerido")
        Long bookId,

        @NotBlank(message = "El nombre del contacto es requerido")
        @Size(max = 255, message = "El nombre del contacto no debe exceder 255 caracteres")
        String contactName,

        @NotNull(message = "La fecha de préstamo es requerida")
        @PastOrPresent(message = "La fecha de préstamo debe ser en el pasado o presente")
        LocalDate loanDate,

        @NotNull(message = "La fecha de vencimiento es requerida")
        @Future(message = "La fecha de vencimiento debe ser en el futuro")
        LocalDate dueDate
) {
}
