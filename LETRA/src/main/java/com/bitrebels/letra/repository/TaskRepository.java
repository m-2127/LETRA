package com.bitrebels.letra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitrebels.letra.model.LeaveRequest;
import com.bitrebels.letra.model.Task;

public interface TaskRepository extends JpaRepository<Task , Long> {
	
	//Page<Comment> findByPostId(Long postId, Pageable pageable);
}
