package com.bitrebels.letra.model.Firebase;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.List;

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
    private List<Long> progressId;

    public Notification() {
    }

    public Notification(String topic, String name, LocalDate date, long leaveReqId , List<Long> progressId ) {
        this.topic = topic;
        this.name = name;
        this.date = date;
        this.leaveReqId = leaveReqId;
        this.progressId = progressId;
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

    public List<Long> getProgressId() {
        return progressId;
    }

    public void setProgressId(List<Long> progressId) {
        this.progressId = progressId;
    }

    public boolean isApproval() {
        return approval;
    }

    public void setApproval(boolean approval) {
        this.approval = approval;
    }
}
