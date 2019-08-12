package com.bitrebels.letra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bitrebels.letra.model.leavequota.AnnualLeave;

@Repository
public interface AnnualRepo extends JpaRepository<AnnualLeave, Long> {
	
	@Query("from AnnualLeave")
    public List<AnnualLeave> getLeaves();

    //consider noOfLeave is member of AnnualLeave.java class
//    public List<AnnualLeave> findByNoOfLeave(int noOfLeave); 
	
}
