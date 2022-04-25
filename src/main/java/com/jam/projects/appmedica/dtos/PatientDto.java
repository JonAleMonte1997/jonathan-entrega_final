package com.jam.projects.appmedica.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jam.projects.appmedica.security.dtos.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class PatientDto {

    private String name;

    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate dateOfBirth;

    private List<VitalSignDto> vitalSignDtoList;

    private UserDto user;
}
