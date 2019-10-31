package com.bitrebels.letra.services.UpdateTask;

import com.bitrebels.letra.message.request.UpdateTask;
import com.bitrebels.letra.model.Employee;
import com.bitrebels.letra.model.Task;
import com.bitrebels.letra.repository.EmployeeRepository;
import com.bitrebels.letra.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AllocateEmpToTask {

    @Autowired
    TaskRepository taskRepo;

    @Autowired
    EmployeeRepository employeeRepo;

    public void allocateEmpToTask(Task task, Employee employee){

        Task actualTask = task;
        Set<Task> taskSet = employee.getTasks();

        taskSet.add(actualTask);

        actualTask.setEmployee(employee);//adding employee to task

        taskRepo.save(actualTask);
        employeeRepo.save(employee);

    }
}
