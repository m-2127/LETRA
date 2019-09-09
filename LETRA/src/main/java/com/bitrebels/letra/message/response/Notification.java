package com.bitrebels.letra.message.response;

import com.bitrebels.letra.model.LeaveRequest;
import com.sun.javaws.progress.Progress;

public class Notification {

	private Progress progress;

	private LeaveRequest leaveRequest;

	public Notification() {
	}

	public Notification(Progress progress, LeaveRequest leaveRequest) {
		this.progress = progress;
		this.leaveRequest = leaveRequest;
	}

	public Progress getProgress() {
		return progress;
	}

	public void setProgress(Progress progress) {
		this.progress = progress;
	}

	public LeaveRequest getLeaveRequest() {
		return leaveRequest;
	}

	public void setLeaveRequest(LeaveRequest leaveRequest) {
		this.leaveRequest = leaveRequest;
	}
}
