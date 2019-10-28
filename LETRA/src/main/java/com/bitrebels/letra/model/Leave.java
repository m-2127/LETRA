package com.bitrebels.letra.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "approved_leave")
public class Leave implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String LeaveType;

    private int noOfManagers;

    @OneToMany(mappedBy = "leave" , cascade = CascadeType.ALL )
    private Set<Description> description = new HashSet<>();

    private int duration;

    private boolean approval;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LeaveStatus status;

    @ManyToMany( cascade = CascadeType.ALL)
    @JoinTable(name = "leave_leaveDate",
            joinColumns = @JoinColumn(name = "approved_leave"),
            inverseJoinColumns = @JoinColumn(name = "leave_date"))
    private Set<LeaveDates> leaveDates = new HashSet<>();

    @ManyToOne(cascade = CascadeType.PERSIST )
    @JoinColumn(name="employee_id")
    @JsonIgnore
    private Employee employee;

    @ManyToMany(cascade = CascadeType.PERSIST )
    @JsonIgnore
    @JoinTable(name = "leave_reportingManager",
            joinColumns = @JoinColumn(name = "approved_leave"),
            inverseJoinColumns = @JoinColumn(name = "reporting_manager"))
    private Set<ReportingManager> reportingManager = new HashSet<>();

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="hrManager_id")
    @JsonIgnore
    private HRManager hrManager;

    @OneToOne
    @JoinColumn(name = "leaveReqId" , referencedColumnName = "leaveReqId")
    @JsonIgnore
    private LeaveRequest leaveRequest;


    public Leave() {
    }

    public Leave(String leaveType, Set<Description> description, int duration, boolean approval, LeaveStatus status, Set<LeaveDates> leaveDates, LeaveRequest leaveRequest) {
        LeaveType = leaveType;
        this.description = description;
        this.duration = duration;
        this.approval = approval;
        this.status = status;
        this.leaveDates = leaveDates;
        this.leaveRequest = leaveRequest;
    }

    public Leave(LeaveStatus status, Employee employee, HRManager hrManager, LeaveRequest leaveRequest,
                 int noOfManagers) {
        this.status = status;
        this.employee = employee;
        this.hrManager = hrManager;
        this.leaveRequest = leaveRequest;
        this.noOfManagers = noOfManagers;
    }



    public Leave(String leaveType, int duration, boolean approval) {
        this.LeaveType = leaveType;
        this.duration = duration;
        this.approval = approval;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLeaveType() {
        return LeaveType;
    }

    public void setLeaveType(String leaveType) {
        LeaveType = leaveType;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isApproval() {
        return approval;
    }

    public void setApproval(boolean approval) {
        this.approval = approval;
    }

    public Set<Description> getDescription() {
        return description;
    }

    public void setDescription(Set<Description> description) {
        this.description = description;
    }

    public Set<LeaveDates> getLeaveDates() {
        return leaveDates;
    }

    public void setLeaveDates(Set<LeaveDates> leaveDates) {
        this.leaveDates = leaveDates;
    }

    public Set<ReportingManager> getReportingManager() {
        return reportingManager;
    }

    public void setReportingManager(Set<ReportingManager> reportingManager) {
        this.reportingManager = reportingManager;
    }

    public HRManager getHrManager() {
        return hrManager;
    }

    public void setHrManager(HRManager hrManager) {
        this.hrManager = hrManager;
    }

    public LeaveRequest getLeaveRequest() {
        return leaveRequest;
    }

    public void setLeaveRequest(LeaveRequest leaveRequest) {
        this.leaveRequest = leaveRequest;
    }

    public LeaveStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveStatus status) {
        this.status = status;
    }

    public int getNoOfManagers() {
        return noOfManagers;
    }

    public void setNoOfManagers(int noOfManagers) {
        this.noOfManagers = noOfManagers;
    }
}
