package com.jam.projects.appmedica.generic;

import org.springframework.dao.EmptyResultDataAccessException;

public class GenericService <T, ID> {

    private final GenericRepository<T, ID> repository;

    protected GenericService(GenericRepository<T, ID> repository){
        this.repository = repository;
    }

    public void delete(ID id) {

        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new EmptyResultDataAccessException("Patient not found", 1);
        }
    }

    public long count() {

        return repository.count();
    }
}
