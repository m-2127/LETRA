package com.bitrebels.letra.repository.firebase;

import com.bitrebels.letra.model.Firebase.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TopicRepo extends JpaRepository<Topic , Long> {

    @Query("SELECT t FROM Topic t WHERE t.topic = :topic")
    Optional<Topic> findByName(@Param("topic") String topic);
}
