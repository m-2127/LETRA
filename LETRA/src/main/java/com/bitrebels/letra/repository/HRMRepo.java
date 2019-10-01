package com.bitrebels.letra.repository;

import com.bitrebels.letra.model.HRManager;
import com.bitrebels.letra.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HRMRepo extends JpaRepository<HRManager , Long> {

}
