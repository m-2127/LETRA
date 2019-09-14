package com.bitrebels.letra.repository;

import com.bitrebels.letra.model.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgressRepo extends JpaRepository<Progress , Long> {
}
