package com.hospital.hospital.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AppointmentDTO {
    private Long id;
    private String doctorName;
    private Integer roomNumber;
    private LocalDateTime appointmentTime;
    private String patientName;
    private boolean cancelled;
}
