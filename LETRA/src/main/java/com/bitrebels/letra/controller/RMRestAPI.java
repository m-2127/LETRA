package com.bitrebels.letra.controller;

import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bitrebels.letra.message.request.ProjectForm;
import com.bitrebels.letra.message.request.TaskForm;
import com.bitrebels.letra.message.response.ResponseMessage;
import com.bitrebels.letra.model.Project;
import com.bitrebels.letra.model.Task;
import com.bitrebels.letra.repository.ProjectRepository;
import com.bitrebels.letra.repository.TaskRepository;

@RequestMapping("/api/rm")
@RestController
public class RMRestAPI {

	@Autowired
	ProjectRepository projectRepo;

	@Autowired
	TaskRepository taskRepo;

	@PostMapping("/addproject")
	@PreAuthorize("hasRole('RM')")
	public ResponseEntity<?> registerUser(@Valid @RequestBody ProjectForm projectForm) {

		// add project details
		Project project = new Project(projectForm.getName(), projectForm.getStartDate(), 
				projectForm.getFinishDate());
		
		Set<Task> tasks = projectForm.getTasks();
		
		projectRepo.save(project);
		
		for(Task task : tasks ) {
			task.setProject(project);
			taskRepo.save(task);
		}
		
		

		return new ResponseEntity<>(new ResponseMessage("Project Details added successfully!"), HttpStatus.OK);
	}

	@PostMapping("/addproject/{projectId}/tasks")
	public ResponseEntity<?> addTask(@PathVariable(value = "projectId") Long projectId,
			@Valid @RequestBody TaskForm taskForm) {
		Project project = projectRepo.findById(projectId).orElseThrow(
				() -> new UsernameNotFoundException("Project not found with -> project Id : " + projectId));

		Task task = new Task(taskForm.getName(), taskForm.getStartDate(), taskForm.getEndDate(),
				taskForm.getDescription());

		task.setProject(project);

		taskRepo.save(task);

		return new ResponseEntity<>(new ResponseMessage("Task Details added successfully!"), HttpStatus.OK);
	}

}
