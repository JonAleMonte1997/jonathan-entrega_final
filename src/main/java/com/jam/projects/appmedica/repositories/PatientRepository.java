package com.jam.projects.appmedica.repositories;

import com.jam.projects.appmedica.entities.Patient;
import com.jam.projects.appmedica.generic.GenericRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends GenericRepository<Patient, Integer> {

    Page<Patient> findAll(Pageable pageable);

    List<Patient> findPatientsByName(String name);
}
