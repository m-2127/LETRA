package com.bitrebels.letra.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long currentProgress;

    private long requiredProgress;

    private long remainingWork;

    public Progress(long currentProgress, long requiredProgress, long remainingWork) { 
        this.currentProgress = currentProgress;
        this.requiredProgress = requiredProgress;
        this.remainingWork = remainingWork;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(long currentProgress) {
        this.currentProgress = currentProgress;
    }

    public long getRequiredProgress() {
        return requiredProgress;
    }

    public void setRequiredProgress(long requiredProgress) {
        this.requiredProgress = requiredProgress;
    }

    public long getRemainingWork() {
        return remainingWork;
    }

    public void setRemainingWork(long remainingWork) {
        this.remainingWork = remainingWork;
    }
}
