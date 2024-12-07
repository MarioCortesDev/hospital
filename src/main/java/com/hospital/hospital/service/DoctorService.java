package com.hospital.hospital.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hospital.hospital.model.Doctor;
import com.hospital.hospital.repository.DoctorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));
    }

    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }
}
