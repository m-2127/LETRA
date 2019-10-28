package com.bitrebels.letra.model;

import javax.persistence.*;

@Entity
public class Description {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String description;

    @ManyToOne(cascade =  CascadeType.PERSIST)
    @JoinColumn(name = "leave_id")
    private Leave leave;

    private String managerName;

    public Description() {
    }

    public Description(String description, String mangerName) {
        this.description = description;
        this.managerName = mangerName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Leave getLeave() {
        return leave;
    }

    public void setLeave(Leave leave) {
        this.leave = leave;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
}
