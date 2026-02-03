package com.devsenior.nmanja.bibliokeep.repository;

import com.devsenior.nmanja.bibliokeep.model.entity.Book;
import com.devsenior.nmanja.bibliokeep.model.entity.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByOwnerId(UUID ownerId);

    Optional<Book> findByIdAndOwnerId(Long id, UUID ownerId);

    @Query("""
            SELECT COUNT(b) FROM Book b
            WHERE b.ownerId = :ownerId
            AND b.status = :status
            """)
    Long countByOwnerIdAndStatus(@Param("ownerId") UUID ownerId, @Param("status") BookStatus status);

    @Query(value = """
            SELECT DISTINCT b.* FROM books b
            LEFT JOIN book_authors ba ON b.id = ba.book_id
            WHERE b.owner_id = :ownerId
            AND (
                LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(b.isbn) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(ba.author) LIKE LOWER(CONCAT('%', :query, '%'))
            )
            """, nativeQuery = true)
    List<Book> searchByOwnerId(@Param("ownerId") UUID ownerId, @Param("query") String query);
}
