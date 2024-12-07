package com.hospital.hospital.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AppointmentRequest {
    @NotNull(message = "El doctor es requerido")
    private Long doctorId;
    
    @NotNull(message = "El consultorio es requerido")
    private Long consultingRoomId;
    
    @NotNull(message = "La fecha y hora son requeridas")
    private LocalDateTime appointmentTime;
    
    @NotBlank(message = "El nombre del paciente es requerido")
    private String patientName;
}