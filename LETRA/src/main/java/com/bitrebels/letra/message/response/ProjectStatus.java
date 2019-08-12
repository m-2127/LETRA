package com.bitrebels.letra.message.response;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.bitrebels.letra.model.Project;
import com.bitrebels.letra.model.Task;

public class ProjectStatus {
	
	private Project project;
	

	public ProjectStatus(Project project ) {
		super();
		this.project = project;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
