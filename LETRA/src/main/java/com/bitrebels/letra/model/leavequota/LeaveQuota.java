package com.bitrebels.letra.model.leavequota;

import com.bitrebels.letra.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

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

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name="user_id ")
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

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
