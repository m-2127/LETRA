//package com.bitrebels.letra.model;
//
//import javax.persistence.Column;
//import javax.persistence.Embeddable;
//import java.io.Serializable;
//import java.util.Objects;
//
//@Embeddable
//public class ProgressKey implements Serializable {
//
//    @Column(name="progress_id")
//    long progressId;
//
//    long leaveRequestId;
//
//    public ProgressKey() {
//    }
//
//    public ProgressKey(long progressId, long leaveRequestId) {
//        this.progressId = progressId;
//        this.leaveRequestId = leaveRequestId;
//    }
//
//    public long getProgressId() {
//        return progressId;
//    }
//
//    public void setProgressId(long progressId) {
//        this.progressId = progressId;
//    }
//
//    public long getLeaveRequestId() {
//        return leaveRequestId;
//    }
//
//    public void setLeaveRequestId(long leaveRequestId) {
//        this.leaveRequestId = leaveRequestId;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        ProgressKey that = (ProgressKey) o;
//        return progressId == that.progressId &&
//                leaveRequestId == that.leaveRequestId;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(progressId, leaveRequestId);
//    }
//}
