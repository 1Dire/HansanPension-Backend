package com.hansanpension.backend.book.controller;

import com.hansanpension.backend.book.dto.BookDTO;
import com.hansanpension.backend.book.dto.BookCreateDTO;
import com.hansanpension.backend.book.service.BookService;
import com.hansanpension.backend.security.JwtTokenProvider;
import com.hansanpension.backend.user.entity.User;
import com.hansanpension.backend.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService bookService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    // 월별 예약 목록 조회
    @GetMapping
    public List<BookDTO> getBookingsByMonth(@RequestParam int year, @RequestParam int month, @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        boolean isAdmin = false;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.replace("Bearer ", "");
            if (jwtTokenProvider.validateToken(token)) {
                String kakaoId = jwtTokenProvider.getSubject(token);
                isAdmin = checkIfAdmin(kakaoId);
                logger.info("User kakaoId: {}, isAdmin: {}", kakaoId, isAdmin);
            }
        } else {
            logger.info("비회원 접근 - 관리자 아님 처리");
        }

        return bookService.getBookingsByMonth(year, month, isAdmin);
    }

    // 관리자 여부 확인
    private boolean checkIfAdmin(String kakaoId) {
        return userRepository.findByKakaoId(kakaoId).map(user -> {
            boolean isAdmin = user.getRole() == User.Role.ADMIN;
            logger.debug("Found user with kakaoId: {}. Role is: {}", kakaoId, user.getRole());
            return isAdmin;
        }).orElse(false);
    }

    // 특정 방의 예약 가능 일수 확인
    @GetMapping("/check-availability")
    public ResponseEntity<Map<String, Object>> checkAvailability(@RequestParam int roomId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        Map<String, Object> result = bookService.checkMaxAvailableDays(roomId, startDate);
        return ResponseEntity.ok(result);
    }

    // 예약 등록 (POST 요청)
    @PostMapping
    public ResponseEntity<BookDTO> createBooking(@RequestBody BookCreateDTO bookCreateDTO, @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        String kakaoId = null;

        // 비회원 예약 처리
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.replace("Bearer ", "");
            if (jwtTokenProvider.validateToken(token)) {
                kakaoId = jwtTokenProvider.getSubject(token);
            }
        }

        // BookCreateDTO -> BookDTO 변환 후 저장
        BookDTO savedBooking = bookService.createBooking(bookCreateDTO, kakaoId);
        return ResponseEntity.ok(savedBooking);
    }


    // 예약 ID로 예약 데이터 조회
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookingById(@PathVariable Long id) {
        BookDTO booking = bookService.getBookingById(id);
        return booking != null ? ResponseEntity.ok(booking) : ResponseEntity.notFound().build();
    }

    // 예약 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id, @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        String kakaoId = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.replace("Bearer ", "");
            if (jwtTokenProvider.validateToken(token)) {
                kakaoId = jwtTokenProvider.getSubject(token);
                // 🔐 관리자 권한 확인
                if (!checkIfAdmin(kakaoId)) {
                    logger.warn("비관리자가 예약 삭제를 시도했습니다. kakaoId: {}", kakaoId);
                    return ResponseEntity.status(403).build(); // Forbidden
                }
            } else {
                return ResponseEntity.status(401).build(); // Unauthorized
            }
        } else {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        boolean deleted = bookService.deleteBooking(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
    // 예약 수정
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBooking(@PathVariable Long id, @RequestBody BookCreateDTO bookCreateDTO, @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        String kakaoId = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.replace("Bearer ", "");
            if (jwtTokenProvider.validateToken(token)) {
                kakaoId = jwtTokenProvider.getSubject(token);
                // 🔐 관리자 체크
                if (!checkIfAdmin(kakaoId)) {
                    logger.warn("비관리자가 예약 수정을 시도했습니다. kakaoId: {}", kakaoId);
                    return ResponseEntity.status(403).build(); // Forbidden
                }
            } else {
                return ResponseEntity.status(401).build(); // Unauthorized
            }
        } else {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        BookDTO updatedBooking = bookService.updateBooking(id, bookCreateDTO, kakaoId);
        return updatedBooking != null ? ResponseEntity.ok(updatedBooking) : ResponseEntity.notFound().build();
    }
    @GetMapping("/my-bookings")
    public ResponseEntity<List<BookDTO>> getMyBookings(
            @RequestHeader(value = "Authorization") String authorizationHeader
    ) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.replace("Bearer ", "");
            if (jwtTokenProvider.validateToken(token)) {
                String kakaoId = jwtTokenProvider.getSubject(token);
                List<BookDTO> bookings = bookService.getBookingsByKakaoIdFromToday(kakaoId);
                return ResponseEntity.ok(bookings);
            }
        }
        return ResponseEntity.badRequest().build();
    }

}
