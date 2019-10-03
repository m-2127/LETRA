package com.bitrebels.letra.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "tasks")
public class Task {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min=3, max = 50)
    private String taskName;

    @NotNull
    private LocalDate taskStartDate;

    @NotNull
    private LocalDate taskEndDate;
    
    @NotBlank
    @Size(min=10, max = 50)
    private String description;

    //current progress in hours
	@NotNull
    private int progress;

	//task duration in hours
	@NotNull
	private int hours;

	private Status status;

	private Timestamp updateTime;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name="project_id" )
	private Project project;

	@ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.PERSIST)
	@JoinColumn(name = "employee_id")
	private Employee employee;

	public Task(String taskName, LocalDate taskStartDate, String description, int duration) {
		super();
		this.taskName = taskName;
		this.taskStartDate = taskStartDate;
		this.description = description;
		this.hours = duration;
	}

	public void removeTask(){

	}
	
	public Task() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public LocalDate getTaskStartDate() {
		return taskStartDate;
	}

	public void setTaskStartDate(LocalDate taskStartDate) {
		this.taskStartDate = taskStartDate;
	}

	public LocalDate getTaskEndDate() {
		return taskEndDate;
	}

	public void setTaskEndDate(LocalDate taskEndDate) {
		this.taskEndDate = taskEndDate;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getProgress() {
		return progress;
	}
	
	public void setProgress(int progress) {
		this.progress = progress;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
}
