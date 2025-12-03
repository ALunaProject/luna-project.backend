package com.luna.lunaproject.services;

import com.luna.lunaproject.entity.User;
import com.luna.lunaproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<User> listarTodos() {
        return repository.findAll();
    }

    public User criarUsuario(User usuario) throws Exception {
        if (repository.existsByEmail(usuario.getEmail())) {
            throw new Exception("Email já cadastrado!");
        }
        if (repository.existsByNickName(usuario.getNickName())) {
            throw new Exception("Nickname já está em uso!");
        }
        return repository.save(usuario);
    }

    public User autenticar(String email, String senha) {
        return repository.findByEmail(email)
                .filter(u -> u.getPasswordHash().equals(senha))
                .orElse(null);
    }
}
