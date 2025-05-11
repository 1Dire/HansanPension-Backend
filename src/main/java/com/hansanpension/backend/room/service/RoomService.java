package com.hansanpension.backend.room.service;

import com.hansanpension.backend.room.repository.RoomRepository;
import org.springframework.stereotype.Service;
import com.hansanpension.backend.room.entity.Room;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElse(null);
    }
}
