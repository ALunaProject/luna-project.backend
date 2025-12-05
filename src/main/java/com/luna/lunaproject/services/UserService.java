package com.luna.lunaproject.services;

import com.luna.lunaproject.dto.UserLoginRequestDTO;
import com.luna.lunaproject.dto.UserRequestDTO;
import com.luna.lunaproject.dto.UserResponseDTO;
import com.luna.lunaproject.entity.User;
import com.luna.lunaproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<UserResponseDTO> findAll() {
        List<User> users = repository.findAll();
        return users.stream().map(UserResponseDTO::new).collect(Collectors.toList());
    }

    public UserResponseDTO createUser(UserRequestDTO request) throws Exception {
        if (repository.existsByEmail(request.getEmail())) {
            throw new Exception("Email already exists!");
        }
        if (repository.existsByNickName(request.getNickName())) {
            throw new Exception("Nickname already in use!");
        }

        User newUser = new User();
        newUser.setNickName(request.getNickName());
        newUser.setEmail(request.getEmail());
        newUser.setPasswordHash(request.getPassword());

        User savedUser = repository.save(newUser);
        return new UserResponseDTO(savedUser);
    }


    public UserResponseDTO updateUser(Long id, UserRequestDTO request) throws Exception {
        User user = repository.findById(id)
                .orElseThrow(() -> new Exception("User not found"));

        user.setNickName(request.getNickName());
        user.setEmail(request.getEmail());

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPasswordHash(request.getPassword());
        }

        User updatedUser = repository.save(user);
        return new UserResponseDTO(updatedUser);
    }

    public void deleteUser(Long id) throws Exception {
        if (!repository.existsById(id)) {
            throw new Exception("User not found");
        }
        repository.deleteById(id);
    }

    public UserResponseDTO authenticate(UserLoginRequestDTO loginData) {
        Optional<User> userOpt = repository.findByEmail(loginData.getEmail());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPasswordHash().equals(loginData.getPassword())) {
                return new UserResponseDTO(user);
            }
        }
        return null;
    }
}