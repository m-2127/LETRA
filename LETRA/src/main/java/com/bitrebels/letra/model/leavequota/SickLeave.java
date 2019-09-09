package com.bitrebels.letra.model.leavequota;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Sick")
public class SickLeave extends LeaveQuota {
	
	@Column(name="total_leaves")
	private int totalLeaves;
	
	@Column(name="remaining_leaves")
	private int remainingLeaves;

	public void reduceRemaining(int x){
		remainingLeaves -= x;
	}

	public void addTaken(int x){
		setLeavesTaken(getLeavesTaken() + x);
	}

	public int getTotalLeaves() {
		return totalLeaves;
	}

	public void setTotalLeaves(int totalLeaves) {
		this.totalLeaves = totalLeaves;
	}

	public int getRemainingLeaves() {
		return remainingLeaves;
	}

	public void setRemainingLeaves(int remainingLeaves) {
		this.remainingLeaves = remainingLeaves;
	}
	
	

}
