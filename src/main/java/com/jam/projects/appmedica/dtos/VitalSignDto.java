package com.jam.projects.appmedica.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VitalSignDto {

    private Integer bloodPressure;

    private Integer heartRate;
}
