package com.bitrebels.letra.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class LeaveDates {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDate date;

    @ManyToMany(mappedBy = "leaveDates", cascade = CascadeType.PERSIST )
    @JsonIgnore
    private Set<Leave> leaves = new HashSet<>();

    public LeaveDates() {
    }

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

    public Set<Leave> getLeaves() {
        return leaves;
    }

    public void setLeaves(Set<Leave> leaves) {
        this.leaves = leaves;
    }
}
