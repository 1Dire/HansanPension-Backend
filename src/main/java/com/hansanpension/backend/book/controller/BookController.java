package com.hansanpension.backend.book.controller;

import com.hansanpension.backend.book.dto.BookDTO;
import com.hansanpension.backend.book.service.BookService;
import com.hansanpension.backend.security.JwtTokenProvider;
import com.hansanpension.backend.user.entity.User;
import com.hansanpension.backend.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);  // Logger 생성

    @Autowired
    private BookService bookService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    // ✅ 반환 타입을 List<BookDTO>로 고쳐야 한다!
    @GetMapping
    public List<BookDTO> getBookingsByMonth(
            @RequestParam int year,
            @RequestParam int month,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader // Authorization 헤더에서 JWT 토큰을 받기
    ) {
        boolean isAdmin = false;  // 기본적으로 관리자 아닌 상태로 설정

        // Authorization 헤더가 있으면 JWT 토큰을 확인하고 유효성을 체크
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.replace("Bearer ", "");
            if (jwtTokenProvider.validateToken(token)) {
                String kakaoId = jwtTokenProvider.getSubject(token);  // JWT에서 kakaoId 추출
                isAdmin = checkIfAdmin(kakaoId);  // 관리자인지 확인
                logger.info("User kakaoId: {}, isAdmin: {}", kakaoId, isAdmin);  // 로그로 확인
            }
        } else {
            logger.info("비회원 접근 - 관리자 아님 처리");
        }

        // 예약 리스트 반환
        return bookService.getBookingsByMonth(year, month, isAdmin);
    }

    // 사용자가 관리자 역할인지 확인하는 메서드
    private boolean checkIfAdmin(String kakaoId) {
        // kakaoId로 사용자를 조회하고, 해당 사용자가 관리자인지 확인
        return userRepository.findByKakaoId(kakaoId)
                .map(user -> {
                    boolean isAdmin = user.getRole() == User.Role.ADMIN;  // Role이 ADMIN이면 true 반환
                    logger.debug("Found user with kakaoId: {}. Role is: {}", kakaoId, user.getRole());  // User의 role 출력
                    return isAdmin;
                })
                .orElse(false); // 사용자가 없으면 false 반환
    }
}
