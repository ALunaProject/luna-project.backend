package com.luna.lunaproject.application.services;

import com.luna.lunaproject.application.dto.user.UserCreateDto;
import com.luna.lunaproject.application.dto.user.UserResponseDto;
import com.luna.lunaproject.application.dto.user.UserUpdateDto;
import com.luna.lunaproject.domain.entity.User;
import com.luna.lunaproject.domain.enums.UserRole;
import com.luna.lunaproject.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ImageUploadService imageUploadService;

    public List<UserResponseDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserResponseDto::new).toList();
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
                user.getRole().getRole(),
                user.getBio()
        );
    }

    public UserResponseDto updateUser(UUID userId, UserUpdateDto userUpdateDto) {
        validateOwnership(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        String newUsername = userUpdateDto.getUsername();
        if (!newUsername.equals(user.getUsername())) {
            User existing = userRepository.findByUsername(newUsername);
            if (existing != null && !existing.getId().equals(userId)) {
                throw new UsernameAlreadyExistsException("Username já está em uso");
            }
        }
        user.setUsername(newUsername);

        // update parcial: campo null = "não mexe" (senão editar nome/bio apagava foto e banner)
        if (userUpdateDto.getProfilePicUrl() != null) {
            user.setProfilePicUrl(userUpdateDto.getProfilePicUrl());
        }
        if (userUpdateDto.getBannerUrl() != null) {
            user.setBannerUrl(userUpdateDto.getBannerUrl());
        }
        if (userUpdateDto.getBio() != null) {
            user.setBio(userUpdateDto.getBio());
        }
        User updatedUser = userRepository.save(user);

        return new UserResponseDto(
                updatedUser.getId(),
                updatedUser.getUsername()
        );
    }

    public String deleteUser(UUID userId) {
        validateOwnership(userId);

        if  (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return "User deleted successfully";
        }else {
            return "User not found with id: " + userId;
        }
    }

    public UserResponseDto updateProfilePicture(UUID userId, MultipartFile file) {
        validateOwnership(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        String imageUrl = imageUploadService.uploadImage(file); //transforma a img em url
        user.setProfilePicUrl(imageUrl);
        User updatedUser = userRepository.save(user);

        return new UserResponseDto(updatedUser.getId(), updatedUser.getUsername());
    }

    public UserResponseDto updateBanner(UUID userId, MultipartFile file) {
        validateOwnership(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        String imageUrl = imageUploadService.uploadImage(file); //transforma a img em url
        user.setBannerUrl(imageUrl);
        User updatedUser = userRepository.save(user);

        return new UserResponseDto(updatedUser.getId(), updatedUser.getUsername());
    }

    private void validateOwnership(UUID userId) {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean isOwner = authenticatedUser.getId().equals(userId);
        boolean isAdmin = authenticatedUser.getRole() == UserRole.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new ForbiddenOperationException("Você não tem permissão para alterar este usuário");
        }
    }
}
