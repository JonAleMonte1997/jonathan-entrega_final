package com.jam.projects.appmedica.services;

import com.jam.projects.appmedica.dtos.PatientDto;
import com.jam.projects.appmedica.dtos.VitalSignDto;
import com.jam.projects.appmedica.entities.Patient;
import com.jam.projects.appmedica.entities.VitalSign;
import com.jam.projects.appmedica.exceptions.MaxSizeListExceededException;
import com.jam.projects.appmedica.generic.GenericService;
import com.jam.projects.appmedica.repositories.PatientRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PatientService extends GenericService<Patient, Integer> {

    private PatientRepository patientRepository;

    protected PatientService(PatientRepository repository) {
        super(repository);
        this.patientRepository = repository;
    }

    public Patient createPatient(PatientDto patientDto) {

        return patientRepository.save(new Patient(patientDto));
    }

    public List<Patient> findAllPatientWithPagination(Integer offset, Integer pageSize) {

        return patientRepository.findAll(PageRequest.of(offset, pageSize)).getContent();
    }

    public List<Patient> findPatientsByName(String name) {

        return patientRepository.findPatientsByName(name);
    }

    public Patient findPatientById(Integer id) {

        Optional<Patient> patient = patientRepository.findById(id);

        if (patient.isEmpty())
            throw new EntityNotFoundException("Patient with id " + id +" not found");

        return patient.get();
    }

    public List<VitalSign> findPatientVitalSignById(Integer id) {

        return findPatientById(id).getVitalSignList();
    }

    public List<VitalSign> addVitalSignByPatientId(Integer id, VitalSignDto vitalSignDto) {

        Patient patient = findPatientById(id);

        patient.getVitalSignList().add(new VitalSign(vitalSignDto));

        return patientRepository.save(patient).getVitalSignList();
    }

    public Patient updatePatientNameAndDateOfBirth(Integer id, PatientDto patientDto) {

        Patient patient = findPatientById(id);

        patient.setName(patientDto.getName());
        patient.setDateOfBirth(patientDto.getDateOfBirth());

        return patientRepository.save(patient);
    }

    public Patient updatePatientVitalSign(Integer id, List<VitalSignDto> vitalSignDtoList) {

        Patient patient = findPatientById(id);

        List<VitalSign> vitalSignList = vitalSignDtoList.stream().map(VitalSign::new).collect(Collectors.toList());

        patient.setVitalSignList(vitalSignList);

        return patientRepository.save(patient);
    }

    /*
        Metodo batch que crea o modifica dependiendo el caso, hasta un máximo de 50 pacientes
     */
    public List<Patient> addPatients(List<Patient> patientList) {

        if (patientList.size() > 50)
            throw new MaxSizeListExceededException("The list should not exceed 50 patients");

        List<Patient> patientsToCreateList = patientList.stream()
                .filter(patient -> patient.getId() == null)
                .collect(Collectors.toList());

        List<Patient> patientsToUpdateList = patientList.stream()
                .filter(patient -> patient.getId() != null)
                .collect(Collectors.toList());

        patientsToUpdateList.forEach(patient -> updatePatient(patient));

        List<Patient> patientsCreatedList = patientRepository.saveAll(patientsToCreateList);

        patientsCreatedList.addAll(patientsToUpdateList);

        return patientsCreatedList;
    }

    private Patient updatePatient(Patient patient) {

        Patient patientToUpdate = findPatientById(patient.getId());

        patientToUpdate.copyAttributes(patient);

        return patientRepository.save(patientToUpdate);
    }
}
