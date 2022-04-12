package com.jam.projects.appmedica.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class PatientDto {

    private String name;

    private LocalDate dateOfBirth;

    private List<VitalSignDto> vitalSignDtoList;
}
