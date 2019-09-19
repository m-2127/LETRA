package com.bitrebels.letra.model.Firebase;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String topic;
    private String body;
    private String name;
    private LocalDate date;
    private boolean approval;
    private long tTL;

    public Notification(String topic, String name, LocalDate date) {
        this.topic = topic;
        this.name = name;
        this.date = date;
    }

    public Notification(String topic, String name, boolean approval) {
        this.topic = topic;
        this.name = name;
        this.approval = approval;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public long gettTL() {
        return tTL;
    }

    public void settTL(long tTL) {
        this.tTL = tTL;
    }

    public boolean isApproval() {
        return approval;
    }

    public void setApproval(boolean approval) {
        this.approval = approval;
    }
}
