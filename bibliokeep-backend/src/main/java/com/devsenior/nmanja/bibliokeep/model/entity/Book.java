package com.devsenior.nmanja.bibliokeep.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "books", indexes = @Index(name = "idx_book_isbn", columnList = "isbn", unique = true))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", insertable = false, updatable = false)
    private User owner;

    @Column(nullable = false, unique = true, length = 13)
    private String isbn; // Can be 10 or 13 digits

    @Column(nullable = false, length = 500)
    private String title;

    @ElementCollection
    @CollectionTable(name = "book_authors", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "author")
    @Builder.Default
    private List<String> authors = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 1000)
    private String thumbnail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private BookStatus status = BookStatus.DESEADO;

    @Column
    private Integer rating;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isLent = false;
}
