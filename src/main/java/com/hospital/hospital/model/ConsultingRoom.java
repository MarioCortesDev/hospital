package com.hospital.hospital.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "consulting_rooms")
@Getter
@Setter
public class ConsultingRoom extends BaseEntity {
    @Column(nullable = false, unique = true)
    private Integer roomNumber;
    
    @Column(nullable = false)
    private Integer floor;
    
    @OneToMany(mappedBy = "consultingRoom", cascade = CascadeType.ALL)
    private List<Appointment> appointments = new ArrayList<>();
}