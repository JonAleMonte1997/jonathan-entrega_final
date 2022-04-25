package com.jam.projects.appmedica.security.entities;

import com.jam.projects.appmedica.security.dtos.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@Table(name = "tbt_user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "user_username")
    private String username;

    @Column(name = "user_password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tbt_users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Collection<RolEntity> rolEntityList;

    public UserEntity(UserDto userDto) {

        this.username = userDto.getUsername();

        this.password = userDto.getPassword();

        this.rolEntityList = new ArrayList<>();
    }

}
