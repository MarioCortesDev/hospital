package com.hospital.hospital.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hospital.hospital.model.ConsultingRoom;
import com.hospital.hospital.repository.ConsultingRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsultingRoomService {
    private final ConsultingRoomRepository consultingRoomRepository;

    public List<ConsultingRoom> getAllRooms() {
        return consultingRoomRepository.findAll();
    }

    public ConsultingRoom saveRoom(ConsultingRoom room) {
        return consultingRoomRepository.save(room);
    }

    public ConsultingRoom getRoomById(Long id) {
        return consultingRoomRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Consultorio no encontrado"));
    }

    public void deleteRoom(Long id) {
        consultingRoomRepository.deleteById(id);
    }
}
