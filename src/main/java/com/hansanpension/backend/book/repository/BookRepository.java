package com.hansanpension.backend.book.repository;

import com.hansanpension.backend.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // checkIn 날짜가 주어진 범위 내에 있는 예약 리스트를 가져오는 메소드
    List<Book> findByStartDateBetween(LocalDate start, LocalDate end);

    // 특정 방의 예약을 시작일 기준으로 정렬해서 가져오는 메소드
    List<Book> findByRoomIdOrderByStartDateAsc(int roomId);
}
