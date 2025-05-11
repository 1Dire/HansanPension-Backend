package com.hansanpension.backend.room.repository;
import com.hansanpension.backend.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
