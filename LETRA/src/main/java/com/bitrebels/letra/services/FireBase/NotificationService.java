package com.bitrebels.letra.services.FireBase;

import com.bitrebels.letra.model.Firebase.Notification;
import org.springframework.http.ResponseEntity;

public interface NotificationService {
    ResponseEntity<?> sendToTopic(Notification notification);
}
