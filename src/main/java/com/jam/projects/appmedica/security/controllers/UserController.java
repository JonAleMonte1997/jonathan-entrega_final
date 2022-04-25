package com.jam.projects.appmedica.security.controllers;

import com.jam.projects.appmedica.security.dtos.RolDto;
import com.jam.projects.appmedica.security.dtos.UserDto;
import com.jam.projects.appmedica.security.entities.RolEntity;
import com.jam.projects.appmedica.security.entities.UserEntity;
import com.jam.projects.appmedica.security.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    public ResponseEntity<List<UserEntity>> findAllUsers() {

        return ResponseEntity.ok(userService.findAllUsers());
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserDto userDto) {

        return ResponseEntity.ok(userService.createUser(userDto));
    }

    @PostMapping("/rol")
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    public ResponseEntity<RolEntity> createRol(@RequestBody RolDto rolDto) {

        return ResponseEntity.ok(userService.createRol(rolDto));
    }

    @PutMapping("/rol")
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    public ResponseEntity<?> addRolToUser(@RequestParam String username, @RequestParam String rolname) {

        userService.addRolToUser(username, rolname);

        return ResponseEntity.ok().build();
    }
}
