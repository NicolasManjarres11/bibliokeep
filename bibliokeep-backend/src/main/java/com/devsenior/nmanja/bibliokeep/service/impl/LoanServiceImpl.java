package com.devsenior.nmanja.bibliokeep.service.impl;

import com.devsenior.nmanja.bibliokeep.mapper.LoanMapper;
import com.devsenior.nmanja.bibliokeep.model.dto.LoanRequestDTO;
import com.devsenior.nmanja.bibliokeep.model.dto.LoanResponseDTO;
import com.devsenior.nmanja.bibliokeep.repository.BookRepository;
import com.devsenior.nmanja.bibliokeep.repository.LoanRepository;
import com.devsenior.nmanja.bibliokeep.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final LoanMapper loanMapper;

    @Override
    public LoanResponseDTO create(UUID ownerId, LoanRequestDTO request) {
        var book = bookRepository.findByIdAndOwnerId(request.bookId(), ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado o no pertenece al usuario"));

        if (book.getIsLent()) {
            throw new IllegalStateException("El libro ya está prestado");
        }

        var loan = loanMapper.toEntity(request);
        loan.setBookId(request.bookId());
        loan.setReturned(false);

        var savedLoan = loanRepository.save(loan);

        book.setIsLent(true);
        bookRepository.save(book);

        return loanMapper.toResponseDTO(savedLoan);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanResponseDTO> findAllByOwnerId(UUID ownerId) {
        var loans = loanRepository.findAllByOwnerId(ownerId);
        return loans.stream()
                .map(loanMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LoanResponseDTO findByIdAndOwnerId(Long id, UUID ownerId) {
        var loan = loanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Préstamo no encontrado"));

        var book = bookRepository.findById(loan.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("Libro asociado no encontrado"));

        if (!book.getOwnerId().equals(ownerId)) {
            throw new IllegalArgumentException("El préstamo no pertenece al usuario");
        }

        return loanMapper.toResponseDTO(loan);
    }

    @Override
    public LoanResponseDTO update(Long id, UUID ownerId, LoanRequestDTO request) {
        var loan = loanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Préstamo no encontrado"));

        var book = bookRepository.findById(loan.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("Libro asociado no encontrado"));

        if (!book.getOwnerId().equals(ownerId)) {
            throw new IllegalArgumentException("El préstamo no pertenece al usuario");
        }

        if (loan.getReturned()) {
            throw new IllegalStateException("No se puede actualizar un préstamo ya devuelto");
        }

        loanMapper.updateEntityFromDTO(request, loan);
        var updatedLoan = loanRepository.save(loan);

        return loanMapper.toResponseDTO(updatedLoan);
    }

    @Override
    public void delete(Long id, UUID ownerId) {
        var loan = loanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Préstamo no encontrado"));

        var book = bookRepository.findById(loan.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("Libro asociado no encontrado"));

        if (!book.getOwnerId().equals(ownerId)) {
            throw new IllegalArgumentException("El préstamo no pertenece al usuario");
        }

        if (!loan.getReturned()) {
            book.setIsLent(false);
            bookRepository.save(book);
        }

        loanRepository.delete(loan);
    }

    @Override
    public LoanResponseDTO returnLoan(Long id, UUID ownerId) {
        var loan = loanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Préstamo no encontrado"));

        var book = bookRepository.findById(loan.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("Libro asociado no encontrado"));

        if (!book.getOwnerId().equals(ownerId)) {
            throw new IllegalArgumentException("El préstamo no pertenece al usuario");
        }

        if (loan.getReturned()) {
            throw new IllegalStateException("El préstamo ya fue devuelto");
        }

        loan.setReturned(true);
        var updatedLoan = loanRepository.save(loan);

        book.setIsLent(false);
        bookRepository.save(book);

        return loanMapper.toResponseDTO(updatedLoan);
    }
}
