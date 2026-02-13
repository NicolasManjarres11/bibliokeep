package com.devsenior.nmanja.bibliokeep.service.impl;

import com.devsenior.nmanja.bibliokeep.mapper.BookMapper;
import com.devsenior.nmanja.bibliokeep.model.dto.BookRequestDTO;
import com.devsenior.nmanja.bibliokeep.model.dto.BookResponseDTO;
import com.devsenior.nmanja.bibliokeep.model.entity.BookStatus;
import com.devsenior.nmanja.bibliokeep.repository.BookRepository;
import com.devsenior.nmanja.bibliokeep.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookResponseDTO create(UUID ownerId, BookRequestDTO request) {
        var book = bookMapper.toEntity(request, ownerId);
        book.setIsLent(false);
        
        if (book.getStatus() == null) {
            book.setStatus(BookStatus.DESEADO);
        }
        
        var savedBook = bookRepository.save(book);
        return bookMapper.toResponseDTO(savedBook);
    }

    @Override
    public List<BookResponseDTO> getAllBooks() {
        var books = bookRepository.findAll().stream()
            .map(b -> bookMapper.toResponseDTO(b))
            .toList();
        return books;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDTO> findAllByOwnerId(UUID ownerId) {
        var books = bookRepository.findByOwnerId(ownerId);
        return books.stream()
                .map(bookMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponseDTO findByIdAndOwnerId(Long id, UUID ownerId) {
        var book = bookRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado o no pertenece al usuario"));
        return bookMapper.toResponseDTO(book);
    }

    @Override
    public BookResponseDTO update(Long id, UUID ownerId, BookRequestDTO request) {
        var book = bookRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado o no pertenece al usuario"));

        if (book.getIsLent()) {
            throw new IllegalStateException("No se puede actualizar un libro que está prestado");
        }

        bookMapper.updateEntityFromDTO(request, book);
        var updatedBook = bookRepository.save(book);

        return bookMapper.toResponseDTO(updatedBook);
    }

    @Override
    public void delete(Long id, UUID ownerId) {
        var book = bookRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado o no pertenece al usuario"));

        if (book.getIsLent()) {
            throw new IllegalStateException("No se puede eliminar un libro que está prestado");
        }

        bookRepository.delete(book);
    }

    @Override
    public BookResponseDTO updateStatus(Long id, UUID ownerId, BookStatus status) {
        var book = bookRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado o no pertenece al usuario"));

        book.setStatus(status);
        var updatedBook = bookRepository.save(book);

        return bookMapper.toResponseDTO(updatedBook);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDTO> search(UUID ownerId, String query) {
        var books = bookRepository.searchByOwnerId(ownerId, query);
        return books.stream()
                .map(bookMapper::toResponseDTO)
                .toList();
    }
}
