package com.jam.projects.appmedica.security.entities;

import com.jam.projects.appmedica.security.dtos.RolDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "tbt_rol")
public class RolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rol_id")
    private Integer id;

    @Column(name = "rol_name")
    private String name;

    public RolEntity(RolDto rolDto) {

        this.name = rolDto.getName();
    }
}
