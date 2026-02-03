package com.devsenior.nmanja.bibliokeep.controller;

import com.devsenior.nmanja.bibliokeep.model.dto.LoanRequestDTO;
import com.devsenior.nmanja.bibliokeep.model.dto.LoanResponseDTO;
import com.devsenior.nmanja.bibliokeep.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoanResponseDTO create(
            @RequestHeader("user-id") String userId,
            @Valid @RequestBody LoanRequestDTO request) {
        return loanService.create(UUID.fromString(userId), request);
    }

    @GetMapping
    public List<LoanResponseDTO> findAll(@RequestHeader("user-id") String userId) {
        return loanService.findAllByOwnerId(UUID.fromString(userId));
    }

    @GetMapping("/{id}")
    public LoanResponseDTO findById(
            @PathVariable Long id,
            @RequestHeader("user-id") String userId) {
        return loanService.findByIdAndOwnerId(id, UUID.fromString(userId));
    }

    @PutMapping("/{id}")
    public LoanResponseDTO update(
            @PathVariable Long id,
            @RequestHeader("user-id") String userId,
            @Valid @RequestBody LoanRequestDTO request) {
        return loanService.update(id, UUID.fromString(userId), request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @RequestHeader("user-id") String userId) {
        loanService.delete(id, UUID.fromString(userId));
    }

    @PatchMapping("/{id}/return")
    public LoanResponseDTO returnLoan(
            @PathVariable Long id,
            @RequestHeader("user-id") String userId) {
        return loanService.returnLoan(id, UUID.fromString(userId));
    }
}
