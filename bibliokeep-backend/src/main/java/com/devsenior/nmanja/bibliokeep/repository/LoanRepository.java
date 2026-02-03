package com.devsenior.nmanja.bibliokeep.repository;

import com.devsenior.nmanja.bibliokeep.model.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("""
            SELECT l FROM Loan l
            JOIN Book b ON l.bookId = b.id
            WHERE b.ownerId = :ownerId
            ORDER BY l.loanDate DESC
            """)
    List<Loan> findAllByOwnerId(@Param("ownerId") UUID ownerId);

    @Query("""
            SELECT l FROM Loan l
            JOIN Book b ON l.bookId = b.id
            WHERE b.ownerId = :ownerId AND l.returned = false
            ORDER BY l.dueDate ASC
            """)
    List<Loan> findActiveLoansByOwnerId(@Param("ownerId") UUID ownerId);

    @Query("""
            SELECT l FROM Loan l
            JOIN Book b ON l.bookId = b.id
            WHERE b.ownerId = :ownerId
            AND l.dueDate < :today
            AND l.returned = false
            """)
    List<Loan> findOverdueLoansByOwnerId(@Param("ownerId") UUID ownerId, @Param("today") LocalDate today);

    @Query("""
            SELECT l FROM Loan l
            WHERE l.dueDate < :today
            AND l.returned = false
            """)
    List<Loan> findAllOverdueLoans(@Param("today") LocalDate today);
}
