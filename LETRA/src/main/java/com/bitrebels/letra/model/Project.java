package com.bitrebels.letra.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;



@Entity
@Table(name = "projects")
public class Project {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="project_id")
    private long id;

    @NotBlank
    private String name;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    //current progress in hours
    @NotNull
    private int progress;

	@Enumerated(EnumType.STRING)
	private Status status;

    @OneToMany(fetch = FetchType.EAGER ,mappedBy = "project")
    private Set<Task> task = new HashSet<>();
    
	@OneToOne(mappedBy = "project" , cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	private ReportingManager rm;

	@ManyToMany(mappedBy = "project", cascade = CascadeType.ALL)
	private Set<Employee> employeeSet = new HashSet<>();
    
    public Project() {}
    
    public Project(String name, LocalDate startDate, LocalDate endDate) {
		super();
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
	}



	public ReportingManager getRm() {
		return rm;
	}

	public void setRm(ReportingManager rm) {
		this.rm = rm;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public Set<Task> getTask() {
		return task;
	}

	public void setTask(Set<Task> task) {
		this.task = task;
	}

	public Set<Employee> getEmployeeSet() {
		return employeeSet;
	}

	public void setEmployeeSet(Set<Employee> employeeSet) {
		this.employeeSet = employeeSet;
	}
}
