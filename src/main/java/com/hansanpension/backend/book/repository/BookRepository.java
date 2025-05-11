package com.hansanpension.backend.book.repository;

import com.hansanpension.backend.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b JOIN FETCH b.room WHERE b.checkIn BETWEEN :start AND :end")
    List<Book> findByCheckInBetween(LocalDate start, LocalDate end);
}
