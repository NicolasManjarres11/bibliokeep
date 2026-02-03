package com.devsenior.nmanja.bibliokeep.service.impl;

import com.devsenior.nmanja.bibliokeep.model.entity.Book;
import com.devsenior.nmanja.bibliokeep.model.entity.Loan;
import com.devsenior.nmanja.bibliokeep.model.entity.User;
import com.devsenior.nmanja.bibliokeep.repository.BookRepository;
import com.devsenior.nmanja.bibliokeep.repository.LoanRepository;
import com.devsenior.nmanja.bibliokeep.repository.UserRepository;
import com.devsenior.nmanja.bibliokeep.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    @Scheduled(cron = "0 0 8 * * *")
    @Transactional(readOnly = true)
    public void sendOverdueLoanNotifications() {
        log.info("Iniciando verificación de préstamos en mora...");
        
        var today = LocalDate.now();
        var overdueLoans = loanRepository.findAllOverdueLoans(today);
        
        if (overdueLoans.isEmpty()) {
            log.info("No se encontraron préstamos en mora");
            return;
        }

        log.info("Se encontraron {} préstamos en mora", overdueLoans.size());

        for (var loan : overdueLoans) {
            try {
                var book = bookRepository.findById(loan.getBookId())
                        .orElse(null);
                
                if (book == null) {
                    log.warn("Libro con ID {} no encontrado para préstamo {}", loan.getBookId(), loan.getId());
                    continue;
                }

                var owner = userRepository.findById(book.getOwnerId())
                        .orElse(null);

                if (owner == null) {
                    log.warn("Usuario con ID {} no encontrado para libro {}", book.getOwnerId(), book.getId());
                    continue;
                }

                sendOverdueNotification(owner, book, loan);
            } catch (Exception e) {
                log.error("Error al procesar notificación para préstamo {}: {}", loan.getId(), e.getMessage(), e);
            }
        }

        log.info("Finalizada verificación de préstamos en mora");
    }

    private void sendOverdueNotification(User owner, Book book, Loan loan) {
        var daysOverdue = LocalDate.now().toEpochDay() - loan.getDueDate().toEpochDay();
        
        log.info("""
                ========================================
                NOTIFICACIÓN DE PRÉSTAMO EN MORA
                ========================================
                Usuario: {} ({})
                Libro: {} (ISBN: {})
                Contacto: {}
                Fecha de préstamo: {}
                Fecha de vencimiento: {}
                Días en mora: {}
                ========================================
                """,
                owner.getEmail(),
                owner.getId(),
                book.getTitle(),
                book.getIsbn(),
                loan.getContactName(),
                loan.getLoanDate(),
                loan.getDueDate(),
                daysOverdue
        );

        // TODO: Implementar envío real de email cuando se configure JavaMailSender
        // mailSender.send(createOverdueEmail(owner, book, loan));
    }
}
