package com.hospital.hospital.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hospital.hospital.dto.AppointmentRequest;
import com.hospital.hospital.exception.AppointmentValidationException;
import com.hospital.hospital.model.Appointment;
import com.hospital.hospital.service.AppointmentService;
import com.hospital.hospital.service.ConsultingRoomService;
import com.hospital.hospital.service.DoctorService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final ConsultingRoomService consultingRoomService;

    @GetMapping("/appointments/create")
    public String showCreateForm(Model model) {
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("rooms", consultingRoomService.getAllRooms());
        model.addAttribute("request", new AppointmentRequest());
        model.addAttribute("content", "appointments/create");
        return "layout/main";
    }

    @PostMapping("/appointments/save")
    public String createAppointment(@ModelAttribute AppointmentRequest request, Model model) {
        System.out.println("Recibiendo solicitud de cita:");
        System.out.println("Doctor ID: " + request.getDoctorId());
        System.out.println("Room ID: " + request.getConsultingRoomId());
        System.out.println("Time: " + request.getAppointmentTime());
        System.out.println("Patient: " + request.getPatientName());
        
        try {
            appointmentService.createAppointment(request);
            return "redirect:/appointments";
        } catch (AppointmentValidationException e) {
            System.out.println("Error al crear cita: " + e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("doctors", doctorService.getAllDoctors());
            model.addAttribute("rooms", consultingRoomService.getAllRooms());
            model.addAttribute("content", "appointments/create");
            return "layout/main";
        }
    }
    @GetMapping("/appointments/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(id);
            model.addAttribute("appointment", appointment);
            model.addAttribute("doctors", doctorService.getAllDoctors());
            model.addAttribute("rooms", consultingRoomService.getAllRooms());
            model.addAttribute("content", "appointments/edit"); 
            return "layout/main"; 
        } catch (AppointmentValidationException e) {
            return "redirect:/appointments";
        }
    }

    @PostMapping("/appointments/edit/{id}")
    public String editAppointment(@PathVariable Long id, 
                                @ModelAttribute AppointmentRequest request, 
                                Model model) {
        try {
            appointmentService.editAppointment(id, request);
            return "redirect:/appointments";
        } catch (AppointmentValidationException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("appointment", appointmentService.getAppointmentById(id));
            model.addAttribute("doctors", doctorService.getAllDoctors());
            model.addAttribute("rooms", consultingRoomService.getAllRooms());
            model.addAttribute("content", "appointments/edit"); 
            return "layout/main"; 
        }
    }

    @GetMapping("/appointments")
    public String listAppointments(@RequestParam(required = false) Long doctorId,
                                 @RequestParam(required = false) LocalDate date,
                                 @RequestParam(required = false) Long roomId,
                                 Model model) {
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("rooms", consultingRoomService.getAllRooms());
        model.addAttribute("appointments", appointmentService.searchAppointments(doctorId, date, roomId));
        model.addAttribute("doctorId", doctorId);
        model.addAttribute("date", date != null ? date : LocalDate.now());
        model.addAttribute("roomId", roomId);
        model.addAttribute("content", "appointments/list"); 
        return "layout/main";
    }

    @PostMapping("/appointments/{id}/cancel")
    public String cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return "redirect:/appointments";
    }
}