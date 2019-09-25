package com.bitrebels.letra.repository;

import java.util.List;
import java.util.Optional;

import com.bitrebels.letra.model.ReportingManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bitrebels.letra.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Boolean existsByMobilenumber(String mobilenumber);
    Boolean existsByEmail(String email);
    Optional<User> findByResetToken(String resetToken);

//    @Query("SELECT u FROM User u left outer join u.hrManager m on u.hrManager = m.hrmId where m.hrmId is null ")
    @Query("SELECT u FROM User u  where not exists(select h from u.hrManager h where u.id= h.hrmId)")
    List<User> findByNonManagers();

}