package com.luna.lunaproject.config;

import com.luna.lunaproject.domain.entity.GameList;
import com.luna.lunaproject.domain.entity.User;
import com.luna.lunaproject.domain.enums.ListType;
import com.luna.lunaproject.domain.enums.UserRole;
import com.luna.lunaproject.domain.repository.GameListRepository;
import com.luna.lunaproject.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminSeeder implements ApplicationRunner {

    private static final Pattern PASSWORD_RULE =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,100}$");

    private final UserRepository userRepository;
    private final GameListRepository listRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username:}")
    private String username;

    @Value("${app.admin.email:}")
    private String email;

    @Value("${app.admin.password:}")
    private String password;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.existsByRole(UserRole.ADMIN)) {
            return;
        }

        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            log.warn("Admin não criado: defina ADMIN_USERNAME, ADMIN_EMAIL e ADMIN_PASSWORD no .env");
            return;
        }

        String normalizedUsername = username.trim();
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);

        if (!PASSWORD_RULE.matcher(password).matches()) {
            log.warn("Admin não criado: ADMIN_PASSWORD deve ter de 8 a 100 caracteres, "
                    + "com maiúscula, minúscula, número e caractere especial");
            return;
        }
        if (normalizedUsername.chars().anyMatch(Character::isWhitespace)) {
            log.warn("Admin não criado: ADMIN_USERNAME não pode ter espaço");
            return;
        }
        if (userRepository.findByUsername(normalizedUsername) != null
                || userRepository.findByEmailIgnoreCase(normalizedEmail) != null) {
            log.warn("Admin não criado: já existe um usuário com esse username ou e-mail");
            return;
        }

        User admin = new User();
        admin.setUsername(normalizedUsername);
        admin.setEmail(normalizedEmail);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setRole(UserRole.ADMIN);
        // bio, foto de perfil e banner ficam vazios (null)
        User saved = userRepository.save(admin);

        listRepository.saveAll(List.of(
                GameList.defaultList(saved, ListType.WISHLIST, "Quero Jogar"),
                GameList.defaultList(saved, ListType.PLAYING, "Jogando"),
                GameList.defaultList(saved, ListType.PLAYED, "Jogado")
        ));

        log.info("Usuário ADMIN '{}' criado com sucesso", normalizedUsername);
    }
}