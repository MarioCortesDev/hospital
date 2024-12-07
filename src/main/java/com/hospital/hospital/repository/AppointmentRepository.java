package com.hospital.hospital.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hospital.hospital.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>{

    @Query("SELECT a FROM Appointment a WHERE " +
           "(:doctorId IS NULL OR a.doctor.id = :doctorId) AND " +
           "(:roomId IS NULL OR a.consultingRoom.id = :roomId) AND " +
           "a.appointmentTime BETWEEN :startTime AND :endTime AND " +
           "a.cancelled = false " +
           "ORDER BY a.appointmentTime")
    List<Appointment> searchAppointments(
        @Param("doctorId") Long doctorId,
        @Param("roomId") Long roomId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );

    Optional<Appointment> findByConsultingRoomIdAndAppointmentTimeAndCancelledFalse(
        Long roomId, LocalDateTime time);

    Optional<Appointment> findByDoctorIdAndAppointmentTimeAndCancelledFalse(
        Long doctorId, LocalDateTime time);

    List<Appointment> findByPatientNameAndAppointmentTimeBetweenAndCancelledFalse(
        String patientName, LocalDateTime start, LocalDateTime end);

    long countByDoctorIdAndAppointmentTimeBetweenAndCancelledFalse(
        long doctorId, LocalDateTime start, LocalDateTime end);

    List<Appointment> findByDoctorIdAndAppointmentTimeBetweenAndCancelledFalse(
        Long doctorId, LocalDateTime start, LocalDateTime end);
}
