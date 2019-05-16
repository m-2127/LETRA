package com.bitrebels.letra.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bitrebels.letra.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByMobilenumber(String mobilenumber);
    Boolean existsByEmail(String email);
    Optional<User> findByResetToken(String resetToken);
    
}