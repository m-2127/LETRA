package com.bitrebels.letra.services.UpdateTask;

import com.bitrebels.letra.message.request.UpdateTask;
import com.bitrebels.letra.model.*;
import com.bitrebels.letra.repository.*;
import com.bitrebels.letra.services.FireBase.TopicService;
import com.bitrebels.letra.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    TopicService topicService;

    public Employee allocateEmployee(UpdateTask updateTask) {

        Employee employee = null;
        Long rmId = userService.authenticatedUser();
        Long projectId = rmRepo.findById(rmId).get().getProject().getId();

        Project actualProject = projectRepo.getOne(projectId);

        Set<Project> project = new HashSet<Project>();
        project.add(actualProject);

       // Task actualTask = taskRepo.findById(updateTask.getTaskId()).get();
      //  Set<Task> task = new HashSet<Task>();
       // task.add(actualTask);

        ReportingManager actualManager = rmRepo.getOne(rmId);
        Set<ReportingManager> manager = new HashSet<>();
        manager.add(actualManager);

        Optional<Employee> optionalemployee = employeeRepo.findById(updateTask.getEmployeeId());
        //if the user is not currently working on a project

        if (!optionalemployee.isPresent()) {
            Optional<User> optionaluser = userRepo.findById(updateTask.getEmployeeId());
            if(optionaluser.isPresent()) {
                User user = optionaluser.get();
                Role userRole = roleRepo.findByName(RoleName.ROLE_EMPLOYEE).get();
                user.getRoles().add(userRole);
                employee = new Employee(project, manager, user.getId());
             //   employee.setTasks(task);


            //    actualTask.setEmployee(employee);//adding employee to task
                actualManager.getEmployees().add(employee);//adding the employee to RM

                //             employeeRepo.save(employee);
                userRepo.save(user);
            }
        }

        //if the user is currently working on a project
        else {
                employee = optionalemployee.get();
                employee.getProject().add(actualProject);
                employee.getManagers().add(actualManager);
            //   employee.getTasks().add(actualTask);

          // actualTask.setEmployee(employee);//adding employee to task
            actualManager.getEmployees().add(employee);//adding the employee to RM

 //           employeeRepo.save(employee);

        }

        actualProject.getEmployeeSet().add(employee);
        actualManager.getEmployees().add(employee);
        //find user
        User user = userRepo.findById(employee.getEmployeeId()).get();


        return employee ;
        //return new ResponseEntity<>(new ResponseMessage("Employee  added successfully!"), HttpStatus.OK);

    }
}