package com.bitrebels.letra.repository.firebase;

import com.bitrebels.letra.model.Firebase.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepo extends JpaRepository<Notification, Long> {
}
