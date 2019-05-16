package com.bitrebels.letra.message.request;

import javax.validation.constraints.NotNull;

import com.bitrebels.letra.model.leavequota.AnnualLeave;
import com.bitrebels.letra.model.leavequota.CasualLeave;
import com.bitrebels.letra.model.leavequota.MaternityLeave;
import com.bitrebels.letra.model.leavequota.NoPayLeave;
import com.bitrebels.letra.model.leavequota.SickLeave;

public class LeaveQuotaForm {

	@NotNull
	private AnnualLeave annualLeave;
	
	@NotNull
	private CasualLeave casualLeave;
	
	private MaternityLeave maternityLeave;

	@NotNull
	private SickLeave sickLeave;
	
	@NotNull
	private NoPayLeave noPayLeave;

	public AnnualLeave getAnnualLeave() {
		return annualLeave;
	}

	public void setAnnualLeave(AnnualLeave annualLeave) {
		this.annualLeave = annualLeave;
	}

	public CasualLeave getCasualLeave() {
		return casualLeave;
	}

	public void setCasualLeave(CasualLeave casualLeave) {
		this.casualLeave = casualLeave;
	}

	public MaternityLeave getMaternityLeave() {
		return maternityLeave;
	}

	public void setMaternityLeave(MaternityLeave maternityLeave) {
		this.maternityLeave = maternityLeave;
	}

	public SickLeave getSickLeave() {
		return sickLeave;
	}

	public void setSickLeave(SickLeave sickLeave) {
		this.sickLeave = sickLeave;
	}

	public NoPayLeave getNoPayLeave() {
		return noPayLeave;
	}

	public void setNoPayLeave(NoPayLeave noPayLeave) {
		this.noPayLeave = noPayLeave;
	}
}
