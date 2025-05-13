package com.hansanpension.backend.book.service;

import com.hansanpension.backend.book.dto.BookDTO;
import com.hansanpension.backend.book.dto.BookCreateDTO;
import com.hansanpension.backend.book.entity.Book;
import com.hansanpension.backend.book.repository.BookRepository;
import com.hansanpension.backend.room.entity.Room;
import com.hansanpension.backend.room.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RoomRepository roomRepository;

    // 월별 예약 목록 조회
    public List<BookDTO> getBookingsByMonth(int year, int month, boolean isAdmin) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        LocalDate today = LocalDate.now();

        List<Book> bookings = bookRepository.findAll().stream()
                .filter(book -> {
                    // 이번 달과 겹치는 예약
                    boolean overlapsWithMonth = !(book.getEndDate().isBefore(startDate) || book.getStartDate().isAfter(endDate));

                    // 오늘이 예약 기간에 포함되는 예약 (5/12 ~ 5/14 이고 오늘이 5/14일이면 포함)
                    boolean containsToday = !book.getStartDate().isAfter(today) && !book.getEndDate().isBefore(today);

                    // 관리자 아닐 경우: 오늘 이전 시작 예약은 안 보이되, 오늘 포함되면 예외
                    if (!isAdmin) {
                        return overlapsWithMonth && (!book.getStartDate().isBefore(today) || containsToday);
                    }

                    // 관리자면 다 보여줘
                    return overlapsWithMonth;
                })
                .collect(Collectors.toList());

        return bookings.stream()
                .map(book -> BookDTO.builder()
                        .id(book.getId())
                        .roomId(Long.valueOf(book.getRoomId()))
                        .roomName(book.getRoom().getName())
                        .startDate(book.getStartDate())
                        .endDate(book.getEndDate())
                        .status(book.getStatus())
                        .createdAt(book.getCreatedAt())
                        .name(book.getName())
                        .memo(book.getMemo())
                        .totalPrice(book.getTotalPrice())
                        .numPeople(book.getNumPeople())
                        .build())
                .collect(Collectors.toList());
    }


    // 예약 생성
    public BookDTO createBooking(BookCreateDTO bookCreateDTO, String kakaoId) {
        Room room = roomRepository.findById(bookCreateDTO.getRoomId())  // roomId를 Long으로 처리
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        Book newBooking = Book.builder()
                .roomId(bookCreateDTO.getRoomId().intValue())  // Long을 Integer로 변환
                .kakaoId(kakaoId)
                .startDate(bookCreateDTO.getStartDate())
                .endDate(bookCreateDTO.getEndDate())
                .numPeople(bookCreateDTO.getNumPeople())
                .totalPrice(bookCreateDTO.getTotalPrice())
                .status("예약됨")
                .createdAt(LocalDateTime.now())
                .name(bookCreateDTO.getName())
                .memo(bookCreateDTO.getMemo())
                .isCharcoalIncluded(bookCreateDTO.getIsCharcoalIncluded()) // ✅ 추가
                .room(room)
                .build();

        Book savedBooking = bookRepository.save(newBooking);

        return BookDTO.builder()
                .id(savedBooking.getId())
                .roomId(savedBooking.getRoomId().longValue())  // Integer를 Long으로 변환
                .roomName(savedBooking.getRoom().getName())
                .startDate(savedBooking.getStartDate())
                .endDate(savedBooking.getEndDate())
                .numPeople(savedBooking.getNumPeople())
                .totalPrice(savedBooking.getTotalPrice())
                .status(savedBooking.getStatus())
                .createdAt(savedBooking.getCreatedAt())
                .name(savedBooking.getName())
                .memo(savedBooking.getMemo())

                .build();
    }

    // 특정 방의 예약 가능 일수 확인
    public Map<String, Object> checkMaxAvailableDays(int roomId, LocalDate startDate) {
        List<Book> bookings = bookRepository.findByRoomIdOrderByStartDateAsc(roomId);

        // 다음 예약만 필터링
        Book nextBooking = bookings.stream()
                .filter(booking -> booking.getStartDate().isAfter(startDate)) // 👉 무조건 startDate 이후만
                .findFirst()
                .orElse(null); // 👉 다음 예약 없으면 null

        int maxDays;
        if (nextBooking == null) {
            maxDays = 14; // 다음 예약 없음 → 14일 가능
        } else {
            maxDays = (int) ChronoUnit.DAYS.between(startDate, nextBooking.getStartDate());
            maxDays = Math.min(maxDays, 14); // 최대 14일 제한
        }

        return Map.of("maxAvailableDays", maxDays);
    }

}
