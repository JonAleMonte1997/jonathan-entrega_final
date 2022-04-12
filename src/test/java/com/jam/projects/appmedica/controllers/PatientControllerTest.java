package com.jam.projects.appmedica.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jam.projects.appmedica.dtos.PatientDto;
import com.jam.projects.appmedica.dtos.VitalSignDto;
import com.jam.projects.appmedica.entities.Patient;
import com.jam.projects.appmedica.entities.VitalSign;
import com.jam.projects.appmedica.services.PatientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "user", password = "user", roles = "USER")
class PatientControllerTest {

    @MockBean
    PatientService patientService;

    @Autowired
    protected MockMvc mockMvc;

    @Test
    void findAllPatientWithPagination() throws Exception {

        List<Patient> patientList = Arrays.asList(new Patient(), new Patient());

        Mockito.when(patientService.findAllPatientWithPagination(0, 5)).thenReturn(patientList);

        this.mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(patientList.size()));
    }

    @Test
    void count() throws Exception {

        long count = 2;

        Mockito.when(patientService.count()).thenReturn(count);

        this.mockMvc.perform(get("/api/patients/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(count));
    }

    @Test
    void findAllPatientByName() throws Exception {

        String name = "Jonathan";

        Patient patientExpected = new Patient();
        patientExpected.setName(name);

        List<Patient> patientExpectedList = Arrays.asList(patientExpected);

        Mockito.when(patientService.findPatientsByName(name)).thenReturn(patientExpectedList);

        this.mockMvc.perform(get("/api/patients/name/"+name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(name));
    }

    @Test
    void findPatientById() throws Exception {

        Integer id = 1;

        Patient patientExpected = new Patient();
        patientExpected.setId(id);

        Mockito.when(patientService.findPatientById(1)).thenReturn(patientExpected);

        this.mockMvc.perform(get("/api/patients/"+id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void findPatientVitalSignById() throws Exception {

        Integer id = 1;

        List<VitalSign> vitalSignExpectedList = Arrays.asList(new VitalSign(), new VitalSign());

        Mockito.when(patientService.findPatientVitalSignById(1)).thenReturn(vitalSignExpectedList);

        this.mockMvc.perform(get("/api/patients/"+id+"/vitalSignList"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(vitalSignExpectedList.size()));
    }

    @Test
    void createPatient() throws Exception {

        PatientDto patientDto = new PatientDto();
        patientDto.setName("Jonathan");

        Patient patientExpected = new Patient(patientDto);

        String patientDtoJson = new ObjectMapper().writeValueAsString(patientDto);

        Mockito.when(patientService.createPatient(patientDto)).thenReturn(patientExpected);

        this.mockMvc
                .perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(patientDto.getName()));
    }

    @Test
    void addVitalSignByPatientId() throws Exception {

        Integer id = 1;

        VitalSignDto vitalSignDto = new VitalSignDto();
        vitalSignDto.setHeartRate(10);

        List<VitalSign> vitalSignListExpected = Arrays.asList(new VitalSign(), new VitalSign(vitalSignDto));

        String vitalSignDtoJson = new ObjectMapper().writeValueAsString(vitalSignDto);

        Mockito.when(patientService.addVitalSignByPatientId(id, vitalSignDto)).thenReturn(vitalSignListExpected);

        this.mockMvc
                .perform(post("/api/patients/" + id + "/vitalSignList")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(vitalSignDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].heartRate").value(vitalSignListExpected.get(1).getHeartRate()));
    }

    @Test
    void addPatients() throws Exception {

        List<Patient> patientList = Arrays.asList(new Patient(), new Patient());

        String patientListJson = new ObjectMapper().writeValueAsString(patientList);

        Mockito.when(patientService.addPatients(patientList)).thenReturn(patientList);

        this.mockMvc
                .perform(post("/api/patients/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientListJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(patientList.size()));
    }

    @Test
    void updatePatientNameAndDateOfBirth() throws Exception {

        Integer id = 1;

        PatientDto patientDto = new PatientDto();
        patientDto.setName("Jonathan");

        Patient patientExpected = new Patient(patientDto);

        String patientDtoJson = new ObjectMapper().writeValueAsString(patientDto);

        Mockito.when(patientService.updatePatientNameAndDateOfBirth(id, patientDto)).thenReturn(patientExpected);

        this.mockMvc
                .perform(put("/api/patients/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(patientExpected.getName()));
    }

    @Test
    void updatePatientVitalSign() throws Exception {

        Integer id = 1;

        List<VitalSignDto> vitalSignDtoList = Arrays.asList(new VitalSignDto(), new VitalSignDto());

        Patient patientExpected = new Patient();
        patientExpected.setVitalSignList(vitalSignDtoList.stream().map(VitalSign::new).collect(Collectors.toList()));

        String vitalSignDtoListJson = new ObjectMapper().writeValueAsString(vitalSignDtoList);

        Mockito.when(patientService.updatePatientVitalSign(id, vitalSignDtoList)).thenReturn(patientExpected);

        this.mockMvc
                .perform(put("/api/patients/" + id + "/vitalSignList")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(vitalSignDtoListJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vitalSignList.length()").value(patientExpected.getVitalSignList().size()));
    }

    @Test
    void deletePatient() throws Exception {

        Integer id = 1;

        this.mockMvc
                .perform(delete("/api/patients/" + id))
                .andExpect(status().isNoContent());
    }
}