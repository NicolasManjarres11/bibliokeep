package com.devsenior.nmanja.bibliokeep.service;

import com.devsenior.nmanja.bibliokeep.model.dto.BookRequestDTO;
import com.devsenior.nmanja.bibliokeep.model.dto.BookResponseDTO;
import com.devsenior.nmanja.bibliokeep.model.entity.BookStatus;

import java.util.List;
import java.util.UUID;

public interface BookService {

    BookResponseDTO create(UUID ownerId, BookRequestDTO request);

    List<BookResponseDTO> getAllBooks();

    List<BookResponseDTO> findAllByOwnerId(UUID ownerId);

    BookResponseDTO findByIdAndOwnerId(Long id, UUID ownerId);

    BookResponseDTO update(Long id, UUID ownerId, BookRequestDTO request);

    void delete(Long id, UUID ownerId);

    BookResponseDTO updateStatus(Long id, UUID ownerId, BookStatus status);

    List<BookResponseDTO> search(UUID ownerId, String query);
}
