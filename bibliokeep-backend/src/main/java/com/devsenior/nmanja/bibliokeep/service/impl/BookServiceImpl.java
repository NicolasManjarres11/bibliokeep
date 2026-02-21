package com.devsenior.nmanja.bibliokeep.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsenior.nmanja.bibliokeep.mapper.BookMapper;
import com.devsenior.nmanja.bibliokeep.model.dto.BookRequestDTO;
import com.devsenior.nmanja.bibliokeep.model.dto.BookResponseDTO;
import com.devsenior.nmanja.bibliokeep.model.entity.BookStatus;
import com.devsenior.nmanja.bibliokeep.model.vo.JwtUser;
import com.devsenior.nmanja.bibliokeep.repository.BookRepository;
import com.devsenior.nmanja.bibliokeep.service.BookService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookResponseDTO create(BookRequestDTO request) {
        var ownerId = getUserID();
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
    public List<BookResponseDTO> findAllByOwnerId() {
        var ownerId = getUserID();
        var books = bookRepository.findByOwnerId(ownerId);
        return books.stream()
                .map(bookMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponseDTO findByIdAndOwnerId(Long id) {
        var ownerId = getUserID();
        var book = bookRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado o no pertenece al usuario"));
        return bookMapper.toResponseDTO(book);
    }

    @Override
    public BookResponseDTO update(Long id, BookRequestDTO request) {
        var ownerId = getUserID();
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
    public void delete(Long id) {
        var ownerId = getUserID();
        var book = bookRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado o no pertenece al usuario"));

        if (book.getIsLent()) {
            throw new IllegalStateException("No se puede eliminar un libro que está prestado");
        }

        bookRepository.delete(book);
    }

    @Override
    public BookResponseDTO updateStatus(Long id, BookStatus status) {
        var ownerId = getUserID();
        var book = bookRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado o no pertenece al usuario"));

        book.setStatus(status);
        var updatedBook = bookRepository.save(book);

        return bookMapper.toResponseDTO(updatedBook);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDTO> search(String query) {
        var ownerId = getUserID();
        var books = bookRepository.searchByOwnerId(ownerId, query);
        return books.stream()
                .map(bookMapper::toResponseDTO)
                .toList();
    }


    private UUID getUserID() {

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.getPrincipal() instanceof JwtUser jwt){
            return UUID.fromString(jwt.getUserId());
        }

        return null;

    }


}
