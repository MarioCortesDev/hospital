package com.hospital.hospital.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hospital.hospital.dto.AppointmentRequest;
import com.hospital.hospital.exception.AppointmentValidationException;
import com.hospital.hospital.model.Appointment;
import com.hospital.hospital.model.ConsultingRoom;
import com.hospital.hospital.model.Doctor;
import com.hospital.hospital.repository.AppointmentRepository;
import com.hospital.hospital.repository.ConsultingRoomRepository;
import com.hospital.hospital.repository.DoctorRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final ConsultingRoomRepository consultingRoomRepository;

    @Transactional
    public Appointment createAppointment(AppointmentRequest request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
            .orElseThrow(() -> new AppointmentValidationException("Doctor not found"));
        
        ConsultingRoom room = consultingRoomRepository.findById(request.getConsultingRoomId())
            .orElseThrow(() -> new AppointmentValidationException("Consulting room not found"));

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setConsultingRoom(room);
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setPatientName(request.getPatientName());

        validateAppointment(appointment);
        return appointmentRepository.save(appointment);
    }

    @Transactional
    public Appointment editAppointment(Long appointmentId, AppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new AppointmentValidationException("Cita no encontrada"));

        if (appointment.getAppointmentTime().isBefore(LocalDateTime.now())) {
            throw new AppointmentValidationException("No se pueden editar citas pasadas");
        }

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
            .orElseThrow(() -> new AppointmentValidationException("Doctor not found"));
        
        ConsultingRoom room = consultingRoomRepository.findById(request.getConsultingRoomId())
            .orElseThrow(() -> new AppointmentValidationException("Consulting room not found"));

        Appointment tempAppointment = new Appointment();
        tempAppointment.setId(appointmentId);
        tempAppointment.setDoctor(doctor);
        tempAppointment.setConsultingRoom(room);
        tempAppointment.setAppointmentTime(request.getAppointmentTime());
        tempAppointment.setPatientName(request.getPatientName());

        validateAppointment(tempAppointment);

        appointment.setDoctor(doctor);
        appointment.setConsultingRoom(room);
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setPatientName(request.getPatientName());

        return appointmentRepository.save(appointment);
    }

    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
            .orElseThrow(() -> new AppointmentValidationException("Cita no encontrada"));
    }

    public List<Appointment> searchAppointments(Long doctorId, LocalDate date, Long roomId) {
        LocalDate searchDate = date != null ? date : LocalDate.now();
        LocalDateTime startTime = searchDate.atStartOfDay();
        LocalDateTime endTime = searchDate.plusDays(1).atStartOfDay();

        return appointmentRepository.searchAppointments(
            doctorId,
            roomId,
            startTime,
            endTime
        );
    }

    private void validateAppointment(Appointment appointment) {
        System.out.println("Validando cita:");
        System.out.println("Doctor ID: " + appointment.getDoctor().getId());
        System.out.println("Room ID: " + appointment.getConsultingRoom().getId());
        System.out.println("Appointment Time: " + appointment.getAppointmentTime());
        System.out.println("Patient Name: " + appointment.getPatientName());
        Long appointmentId = appointment.getId();
        
        if (isRoomOccupied(appointment.getConsultingRoom().getId(),
                          appointment.getAppointmentTime(),
                          appointmentId)) {
            throw new AppointmentValidationException("Room is already occupied at this time");
        }

        if (isDoctorOccupied(appointment.getDoctor().getId(),
                            appointment.getAppointmentTime(),
                            appointmentId)) {
            throw new AppointmentValidationException("Doctor is already booked at this time");
        }

        if (hasPatientNearbyAppointment(appointment.getPatientName(),
                                      appointment.getAppointmentTime(),
                                      appointmentId)) {
            throw new AppointmentValidationException(
                "Patient cannot have appointments less than 2 hours apart");
        }

        if (hasDoctorReachedDailyLimit(appointment.getDoctor().getId(),
                                     appointment.getAppointmentTime(),
                                     appointmentId)) {
            throw new AppointmentValidationException(
                "Doctor cannot have more than 8 appointment per day");
        }
    }

    private boolean isRoomOccupied(Long roomId, LocalDateTime time, Long excludeAppointmentId) {
        Optional<Appointment> existingAppointment = appointmentRepository
            .findByConsultingRoomIdAndAppointmentTimeAndCancelledFalse(roomId, time);
        
        return existingAppointment.isPresent() && 
               !existingAppointment.get().getId().equals(excludeAppointmentId);
    }

    private boolean isDoctorOccupied(Long doctorId, LocalDateTime time, Long excludeAppointmentId) {
        Optional<Appointment> existingAppointment = appointmentRepository
            .findByDoctorIdAndAppointmentTimeAndCancelledFalse(doctorId, time);
        
        return existingAppointment.isPresent() && 
               !existingAppointment.get().getId().equals(excludeAppointmentId);
    }

    private boolean hasPatientNearbyAppointment(String patientName, LocalDateTime time, 
                                              Long excludeAppointmentId) {
        LocalDateTime twoHoursBefore = time.minusHours(2);
        LocalDateTime twoHoursAfter = time.plusHours(2);

        List<Appointment> nearbyAppointments = appointmentRepository
            .findByPatientNameAndAppointmentTimeBetweenAndCancelledFalse(
                patientName, twoHoursBefore, twoHoursAfter);
        
        return nearbyAppointments.stream()
            .anyMatch(apt -> !apt.getId().equals(excludeAppointmentId));
    }

    private boolean hasDoctorReachedDailyLimit(Long doctorId, LocalDateTime time, 
                                             Long excludeAppointmentId) {
        LocalDateTime startOfDay = time.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        
        long appointmentCount = appointmentRepository
            .countByDoctorIdAndAppointmentTimeBetweenAndCancelledFalse(
                doctorId, startOfDay, endOfDay);
        if (excludeAppointmentId != null) {
            appointmentCount--;
        }
        
        return appointmentCount >= 8;
    }

    @Transactional
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new AppointmentValidationException("Appointment not found"));

        if (appointment.getAppointmentTime().isBefore(LocalDateTime.now())) {
            throw new AppointmentValidationException("Cannot cancel past appointments");
        }

        appointment.setCancelled(true);
        appointmentRepository.save(appointment);
    }

    
}