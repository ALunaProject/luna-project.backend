package com.luna.lunaproject.controller;

import com.luna.lunaproject.entity.User;
import com.luna.lunaproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public List<User> listar() {
        return service.listarTodos();
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody User usuario) {
        try {
            User novo = service.criarUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(novo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginData) {
        User user = service.autenticar(loginData.getEmail(), loginData.getPasswordHash());
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso negado");
    }
}
