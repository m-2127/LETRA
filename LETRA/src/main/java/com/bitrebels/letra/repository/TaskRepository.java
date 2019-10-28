package com.bitrebels.letra.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.bitrebels.letra.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task , Long> {

    //Page<Comment> findByPostId(Long postId, Pageable pageable);

    Set<Task> findTaskByEmployeeAndProject(Employee employee, Project project);
    Set<Task> findTaskByProject(Project project);

    @Query("update Task set status = statusValue where project = projectVal")
    List<User> updateTaskStatus(@Param("statusValue") Status statusVal , @Param("projectVal") Project projectVal);
}
