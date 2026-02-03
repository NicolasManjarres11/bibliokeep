package com.devsenior.nmanja.bibliokeep.model.dto;

import com.devsenior.nmanja.bibliokeep.model.entity.BookStatus;

import java.util.List;
import java.util.UUID;

public record BookResponseDTO(
        Long id,
        UUID ownerId,
        String isbn,
        String title,
        List<String> authors,
        String description,
        String thumbnail,
        BookStatus status,
        Integer rating,
        Boolean isLent
) {
}
