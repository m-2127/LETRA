package com.bitrebels.letra.services.FireBase;

import com.bitrebels.letra.model.Firebase.Topic;
import com.bitrebels.letra.model.User;
import com.bitrebels.letra.repository.UserRepository;
import com.bitrebels.letra.repository.firebase.NotificationRepo;
import com.bitrebels.letra.repository.firebase.TopicRepo;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.TopicManagementResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TopicServiceImp implements TopicService {

    private UserRepository userRepo;
    private TopicRepo topicRepo;

    @Autowired
    public TopicServiceImp(UserRepository userRepo, TopicRepo topicRepo) {
        this.userRepo = userRepo;
        this.topicRepo = topicRepo;
    }

    @Override
    @Async
    public ResponseEntity<?> subscribe(String deviceToken, String topicName, User user) {


        Optional<Topic> byName = topicRepo.findByName(topicName);
        if (byName.isPresent()) {
            return new ResponseEntity<>("Existing topic. Please change the topic", HttpStatus.BAD_REQUEST);
        }

        TopicManagementResponse response;
        try {
            List<String> deviceTokens = new ArrayList<>();

            deviceTokens.add(deviceToken);

            response = FirebaseMessaging.getInstance().subscribeToTopic(
                    deviceTokens,
                    topicName
            );
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Topic creation failed due to firebase server error", HttpStatus.EXPECTATION_FAILED);
        }

        Topic topic = new Topic(topicName);
        topic.getUsers().add(user);

        Topic save = topicRepo.save(topic);

        if (save == null) {
            return new ResponseEntity<>("Topic saving failed", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Async
    public ResponseEntity<?> unsubscribe(User user , String topic) {
        TopicManagementResponse response;
        try {
            List<String> deviceTokens = new ArrayList<>();

                deviceTokens.add(user.getDeviceToken());

            response = FirebaseMessaging.getInstance().unsubscribeFromTopic(
                    deviceTokens,
                    topic
            );
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("topic unsubscription failed due to firebase server error", HttpStatus.EXPECTATION_FAILED);
        }


        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
