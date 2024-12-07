package com.hospital.hospital.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hospital.hospital.model.ConsultingRoom;
import com.hospital.hospital.service.ConsultingRoomService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class ConsultingRoomController {
    private final ConsultingRoomService consultingRoomService;

    @GetMapping
    public String listRooms(Model model) {
        model.addAttribute("rooms", consultingRoomService.getAllRooms());
        model.addAttribute("content", "rooms/list");
        return "layout/main";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("room", new ConsultingRoom());
        model.addAttribute("content", "rooms/form");
        return "layout/main";
    }

    @PostMapping("/save")
    public String saveRoom(@ModelAttribute ConsultingRoom room) {
        consultingRoomService.saveRoom(room);
        return "redirect:/rooms";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("room", consultingRoomService.getRoomById(id));
        model.addAttribute("content", "rooms/form");
        return "layout/main";
    }

    @PostMapping("/delete/{id}")
    public String deleteRoom(@PathVariable Long id) {
        consultingRoomService.deleteRoom(id);
        return "redirect:/rooms";
    }
}