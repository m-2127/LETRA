package com.bitrebels.letra.model;

import javax.persistence.*;

@Entity
public class Progress {

    @Id
    private Long progressId;

    private double currentProgress;

    private double requiredProgress;

    private double remainingWork;

    private double availableHoursOfWork;

    private int availableDaysForLeave;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="manager_id")
    private ReportingManager manager;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="leave_id")
    private LeaveRequest leaveRequest;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="hrManager_id")
    private HRManager hrManager;

    public Progress() {
    }

    public Progress(double currentProgress, double requiredProgress, double remainingWork,
                    double availableHoursOfWork, int availableDaysForLeave) {
        this.currentProgress = currentProgress;
        this.requiredProgress = requiredProgress;
        this.remainingWork = remainingWork;
        this.availableDaysForLeave = availableDaysForLeave;
        this.availableHoursOfWork = availableHoursOfWork;
    }

    public Long getProgressId() {
        return progressId;
    }

    public void setProgressId(Long progressId) {
        this.progressId = progressId;
    }

    public double getCurrentProgress() {
        return Math.round(currentProgress*10)/10.0;
    }

    public void setCurrentProgress(double currentProgress) {
        this.currentProgress = currentProgress;
    }

    public double getRequiredProgress() {
        return Math.round(requiredProgress*10)/10.0;
    }

    public void setRequiredProgress(double requiredProgress) {
        this.requiredProgress = requiredProgress;
    }

    public double getRemainingWork() {
        return Math.round(remainingWork*10)/10.0;
    }

    public void setRemainingWork(double remainingWork) {
        this.remainingWork = remainingWork;
    }

    public int getAvailableDaysForLeave() {
        return availableDaysForLeave;
    }

    public void setAvailableDaysForLeave(int availableDaysForLeave) {
        this.availableDaysForLeave = availableDaysForLeave;
    }

    public double getAvailableHoursOfWork() {
        return Math.round(availableHoursOfWork*10)/10.0;
    }

    public void setAvailableHoursOfWork(double availableHoursOfWork) {
        this.availableHoursOfWork = availableHoursOfWork;
    }

    public ReportingManager getManager() {
        return manager;
    }

    public void setManager(ReportingManager manager) {
        this.manager = manager;
    }

    public LeaveRequest getLeaveRequest() {
        return leaveRequest;
    }

    public void setLeaveRequest(LeaveRequest leaveRequest) {
        this.leaveRequest = leaveRequest;
    }

    public HRManager getHrManager() {
        return hrManager;
    }

    public void setHrManager(HRManager hrManager) {
        this.hrManager = hrManager;
    }
}
