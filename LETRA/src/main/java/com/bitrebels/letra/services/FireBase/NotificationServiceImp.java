package com.bitrebels.letra.services.FireBase;

import com.bitrebels.letra.model.Firebase.Notification;
import com.bitrebels.letra.repository.firebase.NotificationRepo;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImp implements NotificationService{

    private final NotificationRepo notificationRepo;

    private final TopicService topicService;

    @Autowired
    public NotificationServiceImp(NotificationRepo notificationRepo, TopicService topicService) {
        this.topicService = topicService;
        this.notificationRepo = notificationRepo;
    }

    //This message sends a notification to RM who is subscribed to the employees topic. Notification is sent when a leave is applied
    @Override
    public ResponseEntity<?> sendToEmployeesTopic(Notification notification) {

        Notification save = notificationRepo.save(notification);
        if (save == null) {
            return new ResponseEntity<>("Notification saving failed", HttpStatus.BAD_REQUEST);
        }


        Message message = Message.builder()
                .putData("name", notification.getName())
                .putData("date", notification.getDate().toString())
                .setTopic(notification.getTopic())
                .build();

        return sendMessage(message);

    }

    public ResponseEntity<?> sendToManagersTopic(Notification notification) {

        Notification save = notificationRepo.save(notification);
        if (save == null) {
            return new ResponseEntity<>("Notification saving failed", HttpStatus.BAD_REQUEST);
        }


        Message message = Message.builder()
                .putData("name", notification.getName())
                .putData("approval", notification.isApproval() + "")
                .setTopic(notification.getTopic())
                .build();

            return sendMessage(message);
    }

    public ResponseEntity<?> sendMessage(Message message){
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            return new ResponseEntity<>("Message sent successfully\n" + response, HttpStatus.OK);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Notification sending failed. Try again", HttpStatus.EXPECTATION_FAILED);
        }
    }

}
