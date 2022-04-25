package com.jam.projects.appmedica.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jam.projects.appmedica.dtos.PatientDto;
import com.jam.projects.appmedica.security.entities.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@Table(name = "tbt_patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserEntity user;

    @Column(name = "patient_name")
    private String name;

    @Column(name = "patient_date_of_birth")
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate dateOfBirth;

    @ElementCollection
    @CollectionTable(name = "tbt_vital_sign", joinColumns = @JoinColumn(name = "patient_id"))
    private List<VitalSign> vitalSignList;

    public Patient(PatientDto patientDto, UserEntity userEntity) {

        this.name = patientDto.getName();
        this.dateOfBirth = patientDto.getDateOfBirth();

        if (patientDto.getVitalSignDtoList() != null)
            this.vitalSignList = patientDto.getVitalSignDtoList().stream().map(VitalSign::new).collect(Collectors.toList());
        else
            this.vitalSignList = new ArrayList<>();

        this.user = userEntity;
    }

    public void copyAttributes(Patient patient) {
        this.name = patient.getName();
        this.dateOfBirth = patient.getDateOfBirth();
        this.vitalSignList = patient.getVitalSignList();
    }
}
