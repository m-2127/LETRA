package com.bitrebels.letra.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;



@Entity
@Table(name = "projects")
public class Project {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="project_id")
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
    
    @NotNull
    private int progress;
    
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="project_id")
    private Set<Task> task = new HashSet<>();
    
	@OneToOne
	@JoinColumn(name="rm_id")
	private ReportingManager rm;
    
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	
    
}
