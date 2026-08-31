package com.luna.lunaproject.application.services;

import com.luna.lunaproject.application.dto.UserCreateDto;
import com.luna.lunaproject.application.dto.UserResponseDto;
import com.luna.lunaproject.application.dto.UserUpdateDto;
import com.luna.lunaproject.domain.entity.User;
import com.luna.lunaproject.domain.enums.UserRole;
import com.luna.lunaproject.domain.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;

    public  UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto createUser(UserCreateDto userCreateDto) {
        User user = new User();
        user.setUsername(userCreateDto.getUsername());
        user.setEmail(userCreateDto.getEmail());
        String encryptedPassword = new BCryptPasswordEncoder().encode(userCreateDto.getPassword());//encriptogata
        user.setPassword(encryptedPassword);
        user.setRole(UserRole.USER);
        User savedUser = userRepository.save(user);

        return new UserResponseDto(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole().getRole()

        );
    }

    public UserResponseDto getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getProfilePicUrl(),
                user.getBannerUrl(),
                user.getRole().getRole()
        );
    }

    public UserResponseDto updateUser(UUID userId, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        user.setUsername(userUpdateDto.getUsername());
        user.setProfilePicUrl(userUpdateDto.getProfilePicUrl());
        user.setBannerUrl(userUpdateDto.getBannerUrl());
        user.setBio(userUpdateDto.getBio());
        User updatedUser = userRepository.save(user);

        return new UserResponseDto(
                updatedUser.getId(),
                updatedUser.getUsername()
        );
    }

    public String deleteUser(UUID userId) {
        if  (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
        }
        return "User not found with id: " + userId;
    }

}
