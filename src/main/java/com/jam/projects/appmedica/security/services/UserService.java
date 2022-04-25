package com.jam.projects.appmedica.security.services;

import com.jam.projects.appmedica.security.dtos.RolDto;
import com.jam.projects.appmedica.security.dtos.UserDto;
import com.jam.projects.appmedica.security.entities.RolEntity;
import com.jam.projects.appmedica.security.entities.UserEntity;
import com.jam.projects.appmedica.security.repositories.RolRepository;
import com.jam.projects.appmedica.security.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final RolRepository rolRepository;

    private final PasswordEncoder passwordEncoder;

    public UserEntity createUser(UserDto userDto) {

        UserEntity userEntity = new UserEntity(userDto);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        log.info("Guardando usuario: " + userEntity);
        return userRepository.save(userEntity);
    }

    public RolEntity createRol(RolDto rolDto){

        RolEntity rolEntity = new RolEntity(rolDto);

        log.info("Guardando rol: " + rolEntity);
        return rolRepository.save(rolEntity);
    }

    public void addRolToUser(String username, String rolname) {

        UserEntity userEntity = findUserByUsername(username);

        RolEntity rolEntity = rolRepository.findByName(rolname).orElseThrow(() -> new EntityNotFoundException("Rol not found"));

        log.info("Usuario: " + userEntity);
        log.info("Agregando rol: " + rolEntity);
        userEntity.getRolEntityList().add(rolEntity);
    }

    public UserEntity findUserByUsername(String username) {

        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found"));

        log.info("Usuario: " +  userEntity.toString());
        return userEntity;
    }

    public List<UserEntity> findAllUsers() {

        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        userEntity.getRolEntityList().forEach(rolEntity -> {
            authorities.add(new SimpleGrantedAuthority(rolEntity.getName()));
        });

        return new User(userEntity.getUsername(), userEntity.getPassword(), authorities);
    }
}
