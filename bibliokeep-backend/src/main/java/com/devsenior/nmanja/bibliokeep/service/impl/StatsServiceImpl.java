package com.devsenior.nmanja.bibliokeep.service.impl;

import com.devsenior.nmanja.bibliokeep.model.dto.DashboardStatsDTO;
import com.devsenior.nmanja.bibliokeep.model.entity.BookStatus;
import com.devsenior.nmanja.bibliokeep.repository.BookRepository;
import com.devsenior.nmanja.bibliokeep.repository.LoanRepository;
import com.devsenior.nmanja.bibliokeep.repository.UserRepository;
import com.devsenior.nmanja.bibliokeep.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;

    @Override
    public DashboardStatsDTO getDashboardStats(UUID ownerId) {
        var user = userRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        var totalBooks = bookRepository.findByOwnerId(ownerId).size();
        var booksRead = bookRepository.countByOwnerIdAndStatus(ownerId, BookStatus.LEIDO).intValue();
        var booksReading = bookRepository.countByOwnerIdAndStatus(ownerId, BookStatus.LEYENDO).intValue();
        
        var activeLoans = loanRepository.findActiveLoansByOwnerId(ownerId).size();
        var overdueLoans = loanRepository.findOverdueLoansByOwnerId(ownerId, LocalDate.now()).size();
        
        var annualGoal = user.getAnnualGoal();
        var booksReadThisYear = booksRead; // Por ahora, asumimos que todos los libros LEIDO cuentan para el año
        
        var progressPercentage = annualGoal > 0 
                ? (booksReadThisYear * 100.0) / annualGoal 
                : 0.0;

        return new DashboardStatsDTO(
                totalBooks,
                booksRead,
                booksReading,
                activeLoans,
                overdueLoans,
                annualGoal,
                booksReadThisYear,
                Math.min(progressPercentage, 100.0)
        );
    }
}
