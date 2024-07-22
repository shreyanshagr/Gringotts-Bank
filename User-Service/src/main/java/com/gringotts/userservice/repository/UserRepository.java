package com.gringotts.userservice.repository;

import com.gringotts.userservice.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByAuthId(String authId);
}