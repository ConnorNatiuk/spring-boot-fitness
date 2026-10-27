package com.natiuk.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.natiuk.userservice.model.User;

public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByEmail(String email);
    Boolean existsByKeycloakId(String userId);

    public User findByEmail(String email);
}
