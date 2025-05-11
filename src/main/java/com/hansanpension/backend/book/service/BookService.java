package com.hansanpension.backend.book.service;

import com.hansanpension.backend.book.dto.BookDTO;
import com.hansanpension.backend.book.entity.Book;
import com.hansanpension.backend.book.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<BookDTO> getBookingsByMonth(int year, int month) {
        LocalDate firstDayOfMonth = LocalDate.of(year, Month.of(month), 1);
        LocalDate lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());

        List<Book> books = bookRepository.findByCheckInBetween(firstDayOfMonth, lastDayOfMonth);

        return books.stream()
                .map(book -> BookDTO.builder()
                        .id(book.getId())
                        .roomId(book.getRoomId())
                        .roomName(book.getRoom() != null ? book.getRoom().getName() : null) // 🛡 null 체크
                        .checkIn(book.getCheckIn())
                        .checkOut(book.getCheckOut())
                        .numPeople(book.getNumPeople())
                        .totalPrice(book.getTotalPrice())
                        .status(book.getStatus())
                        .createdAt(book.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
