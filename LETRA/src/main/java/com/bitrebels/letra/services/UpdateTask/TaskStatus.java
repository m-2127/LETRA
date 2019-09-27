package com.bitrebels.letra.services.UpdateTask;

import com.bitrebels.letra.message.request.UpdateTask;
import com.bitrebels.letra.model.Employee;
import com.bitrebels.letra.model.Status;
import com.bitrebels.letra.model.Task;
import com.bitrebels.letra.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskStatus {

    @Autowired
    EmployeeRepository employeeRepo;

    public void updateStatus(UpdateTask updateTask, Task task ){

        String status = updateTask.getStatus();

        if(status.equalsIgnoreCase("Completed")){
            task.setStatus(Status.COMPLETED);
            Employee employee = employeeRepo.findById(updateTask.getEmployeeId()).get();
            employee.getTasks().remove(task);
        }else if(status.equalsIgnoreCase("Maintenance")){
            task.setStatus(Status.MAINTENANCE);
        }else if(status.equalsIgnoreCase("development")){
            task.setStatus(Status.DEVELOPMENT);
        }

    }
}
