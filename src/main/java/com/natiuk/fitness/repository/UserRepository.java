package com.natiuk.fitness.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.natiuk.fitness.model.User;

public interface UserRepository extends JpaRepository<User, String>{

    boolean existsByEmail(String email);
}
