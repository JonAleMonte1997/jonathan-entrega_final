package com.jam.projects.appmedica.controllers;

import com.jam.projects.appmedica.dtos.PatientDto;
import com.jam.projects.appmedica.dtos.VitalSignDto;
import com.jam.projects.appmedica.entities.Patient;
import com.jam.projects.appmedica.entities.VitalSign;
import com.jam.projects.appmedica.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    PatientService patientService;

    @GetMapping
    public ResponseEntity<List<Patient>> findAllPatientWithPagination(@RequestParam Optional<Integer> offset, @Positive Optional<Integer> pageSize) {

        return ResponseEntity.ok(patientService.findAllPatientWithPagination(offset.orElse(0), pageSize.orElse(5)));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {

        return ResponseEntity.ok(patientService.count());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Patient>> findAllPatientByName(@PathVariable String name) {

        return ResponseEntity.ok(patientService.findPatientsByName(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> findPatientById(@PathVariable Integer id) {

        return ResponseEntity.ok(patientService.findPatientById(id));
    }

    @GetMapping("/{id}/vitalSignList")
    public ResponseEntity<List<VitalSign>> findPatientVitalSignById(@PathVariable Integer id) {

        return ResponseEntity.ok(patientService.findPatientVitalSignById(id));
    }

    @PostMapping
    public ResponseEntity<Patient> createPatient (@RequestBody PatientDto patientDto) {

        return ResponseEntity.ok(patientService.createPatient(patientDto));
    }

    @PostMapping("/{id}/vitalSignList")
    public ResponseEntity<List<VitalSign>> addVitalSignByPatientId(@PathVariable Integer id, @RequestBody VitalSignDto vitalSignDto) {

        return  ResponseEntity.ok(patientService.addVitalSignByPatientId(id, vitalSignDto));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<Patient>> addPatients(@RequestBody List<Patient> patientList) {

        return ResponseEntity.ok(patientService.addPatients(patientList));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatientNameAndDateOfBirth(@PathVariable Integer id, @RequestBody PatientDto patientDto) {

        return  ResponseEntity.ok(patientService.updatePatientNameAndDateOfBirth(id, patientDto));
    }

    @PutMapping("/{id}/vitalSignList")
    public ResponseEntity<Patient> updatePatientVitalSign(@PathVariable Integer id, @RequestBody List<VitalSignDto> vitalSignDtoList) {

        return  ResponseEntity.ok(patientService.updatePatientVitalSign(id, vitalSignDtoList));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatient(@PathVariable Integer id) {

        patientService.delete(id);
    }
}
