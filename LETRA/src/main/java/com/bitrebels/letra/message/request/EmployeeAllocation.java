package com.bitrebels.letra.message.request;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class EmployeeAllocation{

    @NotNull
    private String status;

    private List<Long> addedEmp = new ArrayList<>();

    private List<Long> deletedEmp = new ArrayList<>();

    public EmployeeAllocation() {
    }
    

    public List<Long> getAddedEmp() {
        return addedEmp;
    }

    public void setAddedEmp(List<Long> addedEmp) {
        this.addedEmp = addedEmp;
    }

    public List<Long> getDeletedEmp() {
        return deletedEmp;
    }

    public void setDeletedEmp(List<Long> deletedEmp) {
        this.deletedEmp = deletedEmp;
    }
}

