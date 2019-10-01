package com.bitrebels.letra.model.Firebase;

import com.bitrebels.letra.model.Progress;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String topic;
    private long leaveReqId;//this becomes the id of LeaveRequest.class when leave is applied and it becomes the id
    //of Leave.class when leave is responded by RM or HRM
    private String name;
    private LocalDate date;
    private boolean approval;

    @OneToMany(mappedBy = "notification" )
    private Set<Progress> progress = new HashSet<>();

    public Notification() {
    }

    public Notification(String topic, String name, LocalDate date, long leaveReqId ) {
        this.topic = topic;
        this.name = name;
        this.date = date;
        this.leaveReqId = leaveReqId;
    }

    public Notification(String topic, String name, boolean approval , long leaveReqId ) {
        this.topic = topic;
        this.name = name;
        this.approval = approval;
        this.leaveReqId = leaveReqId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public long getLeaveReqId() {
        return leaveReqId;
    }

    public void setLeaveReqId(long leaveReqId) {
        this.leaveReqId = leaveReqId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Set<Progress> getProgress() {
        return progress;
    }

    public void setProgress(Set<Progress> progress) {
        this.progress = progress;
    }

    public boolean isApproval() {
        return approval;
    }

    public void setApproval(boolean approval) {
        this.approval = approval;
    }
}
