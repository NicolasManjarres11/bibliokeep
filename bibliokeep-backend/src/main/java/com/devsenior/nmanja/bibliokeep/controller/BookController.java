package com.devsenior.nmanja.bibliokeep.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.devsenior.nmanja.bibliokeep.model.dto.BookRequestDTO;
import com.devsenior.nmanja.bibliokeep.model.dto.BookResponseDTO;
import com.devsenior.nmanja.bibliokeep.model.dto.BookStatusUpdateDTO;
import com.devsenior.nmanja.bibliokeep.service.BookService;
import com.devsenior.nmanja.bibliokeep.service.SecurityService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


/* @CrossOrigin("http://localhost:4200") */
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final SecurityService securityService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponseDTO create(
            HttpServletRequest request,
            @Valid @RequestBody BookRequestDTO newBook) {

        var userId = securityService.getCurrentUserId(request);
        return bookService.create( newBook);
    }

    @GetMapping
    public List<BookResponseDTO> findAll(HttpServletRequest request) {
        var userId = securityService.getCurrentUserId(request);
        return bookService.findAllByOwnerId();
    }

/*     @GetMapping
    public List<BookResponseDTO> getAllBooks (){
        return bookService.getAllBooks();
    } */

    @GetMapping("/search")
    public List<BookResponseDTO> search(
            @RequestHeader("user-id") String userId,
            @RequestParam("query") String query) {
        return bookService.search( query);
    }

    @GetMapping("/{id}")
    public BookResponseDTO findById(
            @PathVariable Long id,
            @RequestHeader("user-id") String userId) {
        return bookService.findByIdAndOwnerId(id);
    }

    @PutMapping("/{id}")
    public BookResponseDTO update(
            @PathVariable Long id,
            @RequestHeader("user-id") String userId,
            @Valid @RequestBody BookRequestDTO request) {
        return bookService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @RequestHeader("user-id") String userId) {
        bookService.delete(id);
    }

    @PatchMapping("/{id}/status")
    public BookResponseDTO updateStatus(
            @PathVariable Long id,
            @RequestHeader("user-id") String userId,
            @Valid @RequestBody BookStatusUpdateDTO request) {
        return bookService.updateStatus(id,  request.status());
    }
}
