package com.bitrebels.letra.repository;

import com.bitrebels.letra.model.HRManager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HRMRepo extends JpaRepository<HRManager , Long> {
}
