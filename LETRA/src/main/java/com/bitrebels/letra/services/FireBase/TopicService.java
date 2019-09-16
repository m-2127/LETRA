package com.bitrebels.letra.services.FireBase;

import com.bitrebels.letra.model.User;
import org.springframework.http.ResponseEntity;

public interface TopicService {

    ResponseEntity<?> subscribe(String deviceToken, String topic , User user);

    ResponseEntity<?> unsubscribe(User user , String topic);
}
