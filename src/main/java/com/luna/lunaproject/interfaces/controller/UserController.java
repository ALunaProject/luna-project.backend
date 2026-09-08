package com.luna.lunaproject.interfaces.controller;

import com.luna.lunaproject.application.dto.user.UserCreateDto;
import com.luna.lunaproject.application.dto.user.UserResponseDto;
import com.luna.lunaproject.application.dto.user.UserUpdateDto;
import com.luna.lunaproject.application.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        UserResponseDto response = userService.createUser(userCreateDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable UUID id) {
        UserResponseDto response = userService.getUserById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable UUID id, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        UserResponseDto response = userService.updateUser(id, userUpdateDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable UUID id) {
        userService.deleteUser(id);
        return "User has been deleted";
    }

}
