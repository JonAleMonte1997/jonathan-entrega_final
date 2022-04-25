package com.jam.projects.appmedica.services;

import com.jam.projects.appmedica.dtos.PatientDto;
import com.jam.projects.appmedica.dtos.VitalSignDto;
import com.jam.projects.appmedica.entities.Patient;
import com.jam.projects.appmedica.entities.VitalSign;
import com.jam.projects.appmedica.exceptions.MaxSizeListExceededException;
import com.jam.projects.appmedica.repositories.PatientRepository;
import com.jam.projects.appmedica.security.entities.UserEntity;
import com.jam.projects.appmedica.security.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "patient 1", roles = {"USER"}, password = "patient1")
class PatientServiceTest {

    @InjectMocks
    PatientService patientService;

    @Mock
    PatientRepository patientRepository;

    @Mock
    UserService userService;

    @Test
    void createPatient() {

        PatientDto patientDto = new PatientDto();

        UserEntity userCreated = new UserEntity();

        Patient patientExpected = new Patient(patientDto, userCreated);

        Mockito.when(userService.createUser(patientDto.getUser())).thenReturn(userCreated);

        Mockito.doNothing().when(userService).addRolToUser(userCreated.getUsername(), "ROLE_USER");

        Mockito.when(patientRepository.save(new Patient(patientDto, userCreated))).thenReturn(patientExpected);

        assertThat(patientService.createPatient(patientDto)).isEqualTo(patientExpected);
    }

    @Test
    void findAllPatientWithPagination() {

        List<Patient> patientListExpected = Arrays.asList(new Patient(), new Patient());

        Page<Patient> pagePatient = new PageImpl<Patient>(patientListExpected);

        Mockito.when(patientRepository.findAll(PageRequest.of(0, 5))).thenReturn(pagePatient);

        assertThat(patientService.findAllPatientWithPagination(0, 5)).isEqualTo(patientListExpected);
    }

    @Test
    void findPatientsByName() {

        String name = "Jonathan";

        Patient patientExpected = new Patient();
        patientExpected.setName(name);

        Mockito.when(patientRepository.findPatientsByName(name)).thenReturn(Arrays.asList(patientExpected));

        assertThat(patientService.findPatientsByName(name).get(0)).isEqualTo(patientExpected);
    }

    @Test
    void findPatientById() {

        Integer id = -1;

        Mockito.when(patientRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> patientService.findPatientById(id));
    }

    @Test
    void findPatientVitalSignById() {

        Integer id = 1;

        List<VitalSign> vitalSignListExpected = Arrays.asList(new VitalSign());

        Patient patient = new Patient();
        patient.setVitalSignList(vitalSignListExpected);

        Mockito.when(patientRepository.findById(id)).thenReturn(Optional.of(patient));

        assertThat(patientService.findPatientVitalSignById(id)).isEqualTo(vitalSignListExpected);
    }

    @Test
    void addVitalSignByPatientId() {

        Integer id = 1;

        VitalSignDto vitalSignDto = new VitalSignDto(10, 10);

        Patient patient = new Patient();
        patient.setVitalSignList(new ArrayList<>());

        Patient patientExpected = new Patient();
        patientExpected.copyAttributes(patient);
        patientExpected.getVitalSignList().add(new VitalSign(vitalSignDto));

        Mockito.when(patientRepository.findById(id)).thenReturn(Optional.of(patient));

        Mockito.when(patientRepository.save(patientExpected)).thenReturn(patientExpected);

        assertThat(patientService.addVitalSignByPatientId(id, vitalSignDto)).isEqualTo(patientExpected.getVitalSignList());
    }

    @Test
    void updatePatientNameAndDateOfBirth() {

        Integer id = 1;

        String name = "Jonathan";

        PatientDto patientDto = new PatientDto();
        patientDto.setName(name);

        Patient patientExpected = new Patient();
        patientExpected.setName(name);

        Mockito.when(patientRepository.findById(id)).thenReturn(Optional.of(new Patient()));

        Mockito.when(patientRepository.save(patientExpected)).thenReturn(patientExpected);

        assertThat(patientService.updatePatientNameAndDateOfBirth(id, patientDto)).isEqualTo(patientExpected);
    }

    @Test
    void updatePatientVitalSign() {

        Integer id = 1;

        List<VitalSignDto> vitalSignDtoList = new ArrayList<>();
        vitalSignDtoList.add(new VitalSignDto(10, 10));
        vitalSignDtoList.add(new VitalSignDto(20, 20));

        Patient patientExpected = new Patient();
        patientExpected.setVitalSignList(vitalSignDtoList.stream().map(VitalSign::new).collect(Collectors.toList()));

        Patient patientPersisted = new Patient();
        patientPersisted.setVitalSignList(Arrays.asList(new VitalSign()));

        Mockito.when(patientRepository.findById(id)).thenReturn(Optional.of(patientPersisted));

        Mockito.when(patientRepository.save(patientExpected)).thenReturn(patientExpected);

        assertThat(patientService.updatePatientVitalSign(id, vitalSignDtoList)).isEqualTo(patientExpected);
    }

    @Test
    void addPatients() {

        Integer patientPersistedId = 1;

        List<Patient> patientList = new ArrayList<>();

        List<Patient> patienToCreateList = new ArrayList<>();

        Patient patientToCreate = new Patient();
        patientToCreate.setName("Jonathan");
        patientToCreate.setVitalSignList(new ArrayList<>());

        patienToCreateList.add(patientToCreate);

        Patient patientToUpdate = new Patient();
        patientToUpdate.setId(patientPersistedId);
        patientToUpdate.setVitalSignList(new ArrayList<>());
        patientToUpdate.getVitalSignList().add(new VitalSign());

        Patient patientPersited = new Patient();
        patientPersited.setId(patientPersistedId);

        patientList.add(patientToCreate);
        patientList.add(patientToUpdate);

        Mockito.when(patientRepository.findById(patientPersistedId)).thenReturn(Optional.of(patientPersited));

        Mockito.when(patientRepository.save(patientToUpdate)).thenReturn(patientToUpdate);

        Mockito.when(patientRepository.saveAll(patienToCreateList)).thenReturn(patienToCreateList);

        assertThat(patientService.addPatients(patientList)).isEqualTo(patientList);
    }

    @Test
    void addPatientsThrow() {

        List<Patient> patientList = new ArrayList<>();

        for (int i = 0; i < 60; i++) {
            patientList.add(new Patient());
        }

        assertThrows(MaxSizeListExceededException.class, () -> patientService.addPatients(patientList));
    }
}