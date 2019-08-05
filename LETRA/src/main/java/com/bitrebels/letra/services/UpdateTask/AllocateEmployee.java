package com.bitrebels.letra.services.UpdateTask;

import com.bitrebels.letra.message.request.UpdateTask;
import com.bitrebels.letra.message.response.ResponseMessage;
import com.bitrebels.letra.model.*;
import com.bitrebels.letra.repository.*;
import com.bitrebels.letra.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AllocateEmployee {

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    ProjectRepository projectRepo;

    @Autowired
    AllocateEmployee allocateEmployee;

    @Autowired
    TaskRepository taskRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    RMRepository rmRepo;

    @Autowired
    EmployeeRepository employeeRepo;

    @Autowired
    UserService userService;

    @Autowired
    EndDateDetector endDateDetector;

    public Employee allocateEmployee(UpdateTask updateTask) {

        Employee employee;
        Long rmId = userService.authenticatedUser();

        Project actualProject = projectRepo.getOne(updateTask.getProjectId());

        Set<Project> project = new HashSet<Project>();
        project.add(actualProject);

        Task actualTask = taskRepo.findById(updateTask.getTaskId()).get();
        Set<Task> task = new HashSet<Task>();
        task.add(actualTask);

        ReportingManager actualManager = rmRepo.getOne(rmId);
        Set<ReportingManager> manager = new HashSet<>();
        manager.add(actualManager);

        Optional<Employee> optionalemployee = employeeRepo.findById(updateTask.getEmployeeId());
        //if the user is not currently working on a project

        if (!optionalemployee.isPresent()) {
            Optional<User> optionaluser = userRepo.findById(updateTask.getEmployeeId());
                User user = optionaluser.get();
                Role userRole = roleRepo.findByName(RoleName.ROLE_EMPLOYEE).get();
                user.getRoles().add(userRole);
                employee = new Employee(project, manager, task, user.getId());

                employeeRepo.save(employee);
                actualManager.getEmployees().add(employee);//adding the employee to RM

                userRepo.save(user);
        }

        //if the user is currently working on a project
        else {
                employee = optionalemployee.get();
                employee.getProject().add(actualProject);
                employee.getManagers().add(actualManager);
                employee.getTasks().add(actualTask);

                employeeRepo.save(employee);

                actualManager.getEmployees().add(employee);//adding the employee to RM

        }

        return employee;
        //return new ResponseEntity<>(new ResponseMessage("Employee  added successfully!"), HttpStatus.OK);

    }
}