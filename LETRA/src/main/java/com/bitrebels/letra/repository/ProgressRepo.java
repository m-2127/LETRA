package com.bitrebels.letra.repository;

import com.bitrebels.letra.model.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressRepo extends JpaRepository<Progress , Long> {
}
