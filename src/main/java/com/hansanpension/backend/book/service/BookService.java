package com.hansanpension.backend.book.service;

import com.hansanpension.backend.book.dto.BookDTO;
import com.hansanpension.backend.book.entity.Book;
import com.hansanpension.backend.book.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    // 월별 예약을 가져오는 메소드 (관리자 여부에 따라 날짜 필터링)
    public List<BookDTO> getBookingsByMonth(int year, int month, boolean isAdmin) {
        // 시작일과 종료일 계산
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        // 해당 월에 대한 예약 리스트를 가져옵니다.
        List<Book> bookings = bookRepository.findByCheckInBetween(startDate, endDate);

        // 관리자가 아닐 경우, 지난 날짜는 제외
        if (!isAdmin) {
            LocalDate today = LocalDate.now();
            bookings = bookings.stream()
                    .filter(book -> !book.getCheckIn().isBefore(today))  // 오늘 날짜 이후만 필터링
                    .collect(Collectors.toList());
        }

        // Book 엔티티를 BookDTO로 변환하여 반환
        return bookings.stream()
                .map(book -> BookDTO.builder()
                        .id(book.getId())
                        .roomId(book.getRoomId())
                        .roomName(book.getRoom().getName())  // 방 이름 추가
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
