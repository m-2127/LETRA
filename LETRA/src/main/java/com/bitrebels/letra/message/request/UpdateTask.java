package com.bitrebels.letra.message.request;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UpdateTask {

    Map<Long, Integer> updatedTask = new HashMap<>();

    public Map<Long, Integer> getUpdatedTask() {
        return updatedTask;
    }

    public void setUpdatedTask(Map<Long, Integer> updatedTask) {
        this.updatedTask = updatedTask;
    }
}
