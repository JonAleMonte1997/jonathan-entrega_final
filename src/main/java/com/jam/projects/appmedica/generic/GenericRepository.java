package com.jam.projects.appmedica.generic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GenericRepository <T, ID> extends JpaRepository<T, ID> {

    void deleteById(ID id);
    long count();
}
