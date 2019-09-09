package com.bitrebels.letra.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class LeaveDates {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDate date;

    @ManyToMany(mappedBy = "dates", cascade = CascadeType.ALL)
    private List<Leave> leaves = new ArrayList<>();

    public LeaveDates(LocalDate date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Leave> getLeaves() {
        return leaves;
    }

    public void setLeaves(List<Leave> leaves) {
        this.leaves = leaves;
    }
}
