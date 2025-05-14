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

        List<Book> bookings = bookRepository.findAll().stream().filter(book -> {
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
        }).collect(Collectors.toList());

        return bookings.stream().map(book -> BookDTO.builder().id(book.getId()).roomId(Long.valueOf(book.getRoomId())).roomName(book.getRoom().getName()).startDate(book.getStartDate()).endDate(book.getEndDate()).status(book.getStatus()).createdAt(book.getCreatedAt()).name(book.getName()).memo(book.getMemo()).totalPrice(book.getTotalPrice()).numPeople(book.getNumPeople()).build()).collect(Collectors.toList());
    }


    // 예약 생성
    public BookDTO createBooking(BookCreateDTO bookCreateDTO, String kakaoId) {
        Room room = roomRepository.findById(bookCreateDTO.getRoomId())  // roomId를 Long으로 처리
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        Book newBooking = Book.builder().roomId(bookCreateDTO.getRoomId().intValue())  // Long을 Integer로 변환
                .kakaoId(kakaoId).startDate(bookCreateDTO.getStartDate()).endDate(bookCreateDTO.getEndDate()).numPeople(bookCreateDTO.getNumPeople()).totalPrice(bookCreateDTO.getTotalPrice()).phoneNumber(bookCreateDTO.getPhoneNumber()).status("예약됨").createdAt(LocalDateTime.now()).name(bookCreateDTO.getName()).memo(bookCreateDTO.getMemo()).isCharcoalIncluded(bookCreateDTO.getIsCharcoalIncluded()) // ✅ 추가
                .room(room).build();

        Book savedBooking = bookRepository.save(newBooking);

        return BookDTO.builder().id(savedBooking.getId()).roomId(savedBooking.getRoomId().longValue())  // Integer를 Long으로 변환
                .roomName(savedBooking.getRoom().getName()).startDate(savedBooking.getStartDate()).endDate(savedBooking.getEndDate()).numPeople(savedBooking.getNumPeople()).phoneNumber(savedBooking.getPhoneNumber()).totalPrice(savedBooking.getTotalPrice()).status(savedBooking.getStatus()).createdAt(savedBooking.getCreatedAt()).name(savedBooking.getName()).memo(savedBooking.getMemo())

                .build();
    }

    // 특정 방의 예약 가능 일수 확인
    public Map<String, Object> checkMaxAvailableDays(int roomId, LocalDate startDate) {
        List<Book> bookings = bookRepository.findByRoomIdOrderByStartDateAsc(roomId);

        // 다음 예약만 필터링
        Book nextBooking = bookings.stream().filter(booking -> booking.getStartDate().isAfter(startDate)) // 👉 무조건 startDate 이후만
                .findFirst().orElse(null); // 👉 다음 예약 없으면 null

        int maxDays;
        if (nextBooking == null) {
            maxDays = 14; // 다음 예약 없음 → 14일 가능
        } else {
            maxDays = (int) ChronoUnit.DAYS.between(startDate, nextBooking.getStartDate());
            maxDays = Math.min(maxDays, 14); // 최대 14일 제한
        }

        return Map.of("maxAvailableDays", maxDays);
    }


    // 예약 ID로 예약 데이터 조회
    public BookDTO getBookingById(Long id) {
        Book booking = bookRepository.findById(id).orElse(null);

        if (booking == null) {
            return null; // 예약이 존재하지 않으면 null 반환
        }

        return BookDTO.builder().id(booking.getId()).roomId(Long.valueOf(booking.getRoomId())).roomName(booking.getRoom().getName()).startDate(booking.getStartDate()).endDate(booking.getEndDate()).status(booking.getStatus()).createdAt(booking.getCreatedAt()).name(booking.getName()).phoneNumber(booking.getPhoneNumber()).isCharcoalIncluded(booking.getIsCharcoalIncluded()).memo(booking.getMemo()).totalPrice(booking.getTotalPrice()).numPeople(booking.getNumPeople()).build();
    }

    public boolean deleteBooking(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public BookDTO updateBooking(Long id, BookCreateDTO bookCreateDTO, String kakaoId) {
        Book existingBooking = bookRepository.findById(id).orElse(null);

        if (existingBooking == null) {
            return null;
        }

        Room room = roomRepository.findById(bookCreateDTO.getRoomId()).orElseThrow(() -> new IllegalArgumentException("Room not found"));

        // 상태는 주어진 그대로 사용
        existingBooking.setStatus(bookCreateDTO.getStatus());

        existingBooking.setRoomId(bookCreateDTO.getRoomId().intValue());
        existingBooking.setStartDate(bookCreateDTO.getStartDate());
        existingBooking.setEndDate(bookCreateDTO.getEndDate());
        existingBooking.setNumPeople(bookCreateDTO.getNumPeople());
        existingBooking.setTotalPrice(bookCreateDTO.getTotalPrice());
        existingBooking.setPhoneNumber(bookCreateDTO.getPhoneNumber());
        existingBooking.setName(bookCreateDTO.getName());
        existingBooking.setMemo(bookCreateDTO.getMemo());
        existingBooking.setIsCharcoalIncluded(bookCreateDTO.getIsCharcoalIncluded());
        existingBooking.setRoom(room);
        if (kakaoId != null) {
            existingBooking.setKakaoId(kakaoId);
        }

        Book savedBooking = bookRepository.save(existingBooking);

        return BookDTO.builder().id(savedBooking.getId()).roomId(savedBooking.getRoomId().longValue()).roomName(savedBooking.getRoom().getName()).startDate(savedBooking.getStartDate()).endDate(savedBooking.getEndDate()).numPeople(savedBooking.getNumPeople()).totalPrice(savedBooking.getTotalPrice()).status(savedBooking.getStatus())  // 주어진 status 그대로
                .phoneNumber(savedBooking.getPhoneNumber()).name(savedBooking.getName()).memo(savedBooking.getMemo()).createdAt(savedBooking.getCreatedAt()).isCharcoalIncluded(savedBooking.getIsCharcoalIncluded()).build();
    }

    // 오늘 이후 예약(오늘 포함) - kakaoId로 조회
    public List<BookDTO> getBookingsByKakaoIdFromToday(String kakaoId) {
        LocalDate today = LocalDate.now();
        List<Book> bookings = bookRepository.findByKakaoIdAndEndDateAfterOrEndDateEquals(kakaoId, today, today);

        return bookings.stream().map(book ->
                BookDTO.builder()
                        .id(book.getId())
                        .roomId(book.getRoomId().longValue())
                        .roomName(book.getRoom().getName())
                        .startDate(book.getStartDate())
                        .endDate(book.getEndDate())
                        .numPeople(book.getNumPeople())
                        .totalPrice(book.getTotalPrice())
                        .status(book.getStatus())
                        .createdAt(book.getCreatedAt())
                        .phoneNumber(book.getPhoneNumber())
                        .name(book.getName())
                        .memo(book.getMemo())
                        .isCharcoalIncluded(book.getIsCharcoalIncluded())
                        .build()
        ).collect(Collectors.toList());
    }
}
