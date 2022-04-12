package com.jam.projects.appmedica.entities;

import com.jam.projects.appmedica.dtos.VitalSignDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Data
@NoArgsConstructor
public class VitalSign {

    private Integer bloodPressure;

    private Integer heartRate;

    public VitalSign(VitalSignDto vitalSignDto) {
        this.heartRate = vitalSignDto.getHeartRate();
        this.bloodPressure = vitalSignDto.getBloodPressure();
    }
}
