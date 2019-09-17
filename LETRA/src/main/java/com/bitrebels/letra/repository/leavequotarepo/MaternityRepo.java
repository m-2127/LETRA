package com.bitrebels.letra.repository.leavequotarepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bitrebels.letra.model.leavequota.AnnualLeave;
import com.bitrebels.letra.model.leavequota.MaternityLeave;

@Repository
public interface MaternityRepo extends JpaRepository<MaternityLeave, Long> {
	
	@Query("from MaternityLeave")
    public List<MaternityLeave> getLeaves();

    //consider noOfLeave is member of AnnualLeave.java class
//    public List<AnnualLeave> findByNoOfLeave(int noOfLeave); 
	
}
