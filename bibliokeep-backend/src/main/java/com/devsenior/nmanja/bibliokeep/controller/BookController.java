package com.devsenior.nmanja.bibliokeep.controller;

import com.devsenior.nmanja.bibliokeep.model.dto.BookRequestDTO;
import com.devsenior.nmanja.bibliokeep.model.dto.BookResponseDTO;
import com.devsenior.nmanja.bibliokeep.model.dto.BookStatusUpdateDTO;
import com.devsenior.nmanja.bibliokeep.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponseDTO create(
            @RequestHeader("user-id") String userId,
            @Valid @RequestBody BookRequestDTO request) {
        return bookService.create(UUID.fromString(userId), request);
    }

    @GetMapping
    public List<BookResponseDTO> findAll(@RequestHeader("user-id") String userId) {
        return bookService.findAllByOwnerId(UUID.fromString(userId));
    }

    @GetMapping("/search")
    public List<BookResponseDTO> search(
            @RequestHeader("user-id") String userId,
            @RequestParam("query") String query) {
        return bookService.search(UUID.fromString(userId), query);
    }

    @GetMapping("/{id}")
    public BookResponseDTO findById(
            @PathVariable Long id,
            @RequestHeader("user-id") String userId) {
        return bookService.findByIdAndOwnerId(id, UUID.fromString(userId));
    }

    @PutMapping("/{id}")
    public BookResponseDTO update(
            @PathVariable Long id,
            @RequestHeader("user-id") String userId,
            @Valid @RequestBody BookRequestDTO request) {
        return bookService.update(id, UUID.fromString(userId), request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @RequestHeader("user-id") String userId) {
        bookService.delete(id, UUID.fromString(userId));
    }

    @PatchMapping("/{id}/status")
    public BookResponseDTO updateStatus(
            @PathVariable Long id,
            @RequestHeader("user-id") String userId,
            @Valid @RequestBody BookStatusUpdateDTO request) {
        return bookService.updateStatus(id, UUID.fromString(userId), request.status());
    }
}
