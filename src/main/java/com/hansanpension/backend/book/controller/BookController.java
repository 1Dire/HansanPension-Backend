package com.hansanpension.backend.book.controller;

import com.hansanpension.backend.book.dto.BookDTO;
import com.hansanpension.backend.book.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // ✅ 반환 타입을 List<BookDTO>로 고쳐야 한다!
    @GetMapping
    public List<BookDTO> getBookingsByMonth(
            @RequestParam int year,
            @RequestParam int month
    ) {
        return bookService.getBookingsByMonth(year, month);
    }
}
