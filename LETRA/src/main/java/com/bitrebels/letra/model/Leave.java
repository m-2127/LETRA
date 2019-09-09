package com.bitrebels.letra.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "approved_leave")
public class Leave implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String LeaveType;

    private String description;

    private int duration;

    private boolean approval;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "leave_leaveDate",
            joinColumns = @JoinColumn(name = "approved_leave"),
            inverseJoinColumns = @JoinColumn(name = "leave_date"))
    private List<LeaveDates> dates;

    @ManyToOne
    @JoinColumn(name="employee_id")
    private Employee employee;

    public Leave(String leaveType, String description, int duration, boolean approval) {
        LeaveType = leaveType;
        this.description = description;
        this.duration = duration;
        this.approval = approval;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<LeaveDates> getDates() {
        return dates;
    }

    public void setDates(List<LeaveDates> dates) {
        this.dates = dates;
    }

    public String getLeaveType() {
        return LeaveType;
    }

    public void setLeaveType(String leaveType) {
        LeaveType = leaveType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
