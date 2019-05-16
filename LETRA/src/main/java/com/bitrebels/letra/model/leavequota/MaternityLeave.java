package com.bitrebels.letra.model.leavequota;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Maternity")
public class MaternityLeave extends LeaveQuota {
	
	@Column(name="total_leaves")
	private int totalLeaves;

	public int getTotalLeaves() {
		return totalLeaves;
	}

	public void setTotalLeaves(int totalLeaves) {
		this.totalLeaves = totalLeaves;
	}

}
