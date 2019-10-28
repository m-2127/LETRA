package com.bitrebels.letra.services;

import com.bitrebels.letra.model.*;
import com.bitrebels.letra.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UpdateProject {

    @Autowired
    EmployeeRepository employeeRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    RMRepository rmRepo;

    @Autowired
    ProjectRepository projectRepo;

    @Autowired
    TaskRepository taskRepo;

    public void addEmployees(ReportingManager reportingManager, Project project, List<Long> addedEmployees) {
        for (Long addedEmployee : addedEmployees) {
            Optional<Employee> optionalEmployee = employeeRepo.findById(addedEmployee);
            //if the user is not currently working on a project
            Employee employee;

            if (!optionalEmployee.isPresent()) {
                User user = userRepo.findById(addedEmployee).get();

                Role userRole = roleRepo.findByName(RoleName.ROLE_EMPLOYEE).get();
                user.getRoles().add(userRole);

                Set<ReportingManager> managerSet = new HashSet<>();
                managerSet.add(reportingManager);


                Set<Project> projectSet = new HashSet<>();
                projectSet.add(project);

                employee = new Employee(projectSet, managerSet, user.getId());

                userRepo.save(user);
            }
            //if the user is currently working on a project
            else {
                employee = optionalEmployee.get();
                employee.getProject().add(project);
                employee.getManagers().add(reportingManager);
            }

            reportingManager.getEmployees().add(employee);
            project.getEmployeeSet().add(employee);
            employeeRepo.save(employee);
        }
    }

    public void deleteEmployees(ReportingManager reportingManager, Project project, List<Long> deletedEmployees){
        for (Long deletedEmployee : deletedEmployees) {
            Employee employee = employeeRepo.findById(deletedEmployee).get();
            project.getEmployeeSet().remove(employee);
            reportingManager.getEmployees().remove(employee);
            //taskRepo.ge
            projectRepo.save(project);
            rmRepo.save(reportingManager);
        }
    }

    public void updateProjectStatusAndTasks(String taskStatus , Project project){

        if(taskStatus.equalsIgnoreCase("completed")) {
            project.setStatus(Status.COMPLETED);
            taskRepo.updateTaskStatus(Status.COMPLETED , project);
        }
        else if(taskStatus.equalsIgnoreCase("development")){
            project.setStatus(Status.DEVELOPMENT);
            taskRepo.updateTaskStatus(Status.DEVELOPMENT , project);
        }
        else{
            project.setStatus(Status.MAINTENANCE);
            taskRepo.updateTaskStatus(Status.MAINTENANCE , project);
        }
    }
}
