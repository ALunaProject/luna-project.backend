package com.luna.lunaproject.domain.repository;

import com.luna.lunaproject.domain.entity.User;
import com.luna.lunaproject.domain.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername (String username);

    User findByEmailIgnoreCase(String email);

    boolean existsByRole(UserRole role);

}
