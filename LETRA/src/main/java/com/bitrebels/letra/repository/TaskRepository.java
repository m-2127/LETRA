package com.bitrebels.letra.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.bitrebels.letra.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bitrebels.letra.model.LeaveRequest;
import com.bitrebels.letra.model.Project;
import com.bitrebels.letra.model.Task;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task , Long> {

    //Page<Comment> findByPostId(Long postId, Pageable pageable);

    Set<Task> findTaskByEmployeeAndProject(Employee employee, Project project);
}
