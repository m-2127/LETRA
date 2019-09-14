package com.bitrebels.letra.repository;

import com.bitrebels.letra.model.HRManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HRMRepo extends JpaRepository<HRManager , Long> {
}
