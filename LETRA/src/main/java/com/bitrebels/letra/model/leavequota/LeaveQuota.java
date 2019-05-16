package com.bitrebels.letra.model.leavequota;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.bitrebels.letra.model.User;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
		name="Leave_Type",
		discriminatorType=DiscriminatorType.STRING
		)
public class LeaveQuota {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "leave_quota_id")
	private Long id;
	
	private int leavesTaken;
	
//	@OneToOne
//	@JoinColumn(name="user_id ")
//	private User user;
//
//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getLeavesTaken() {
		return leavesTaken;
	}

	public void setLeavesTaken(int leavesTaken) {
		this.leavesTaken = leavesTaken;
	}
}
