package com.devsenior.nmanja.bibliokeep.service;

import java.util.List;

import com.devsenior.nmanja.bibliokeep.model.dto.BookRequestDTO;
import com.devsenior.nmanja.bibliokeep.model.dto.BookResponseDTO;
import com.devsenior.nmanja.bibliokeep.model.entity.BookStatus;

public interface BookService {

    BookResponseDTO create( BookRequestDTO request);

    List<BookResponseDTO> getAllBooks();

    List<BookResponseDTO> findAllByOwnerId();

    BookResponseDTO findByIdAndOwnerId(Long id );

    BookResponseDTO update(Long id,  BookRequestDTO request);

    void delete(Long id );

    BookResponseDTO updateStatus(Long id,  BookStatus status);

    List<BookResponseDTO> search( String query);
}
