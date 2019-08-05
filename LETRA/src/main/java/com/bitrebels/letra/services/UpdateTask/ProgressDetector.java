package com.bitrebels.letra.services.UpdateTask;

import com.bitrebels.letra.message.request.UpdateTask;
import com.bitrebels.letra.model.Task;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class ProgressDetector {
    public Task updateProgress(UpdateTask updateTask, Task task){
        long previousProgress = task.getProgress();
        long updatedProgress = updateTask.getProgress();

        if(previousProgress!=updatedProgress){
            Timestamp timestamp = new Timestamp(new Date().getTime());
            task.setUpdateTime(timestamp);
        }
        task.setProgress(updateTask.getProgress());
        return  task;
    }
}
