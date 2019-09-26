package com.bitrebels.letra.model.Firebase;

import com.bitrebels.letra.model.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long topic_id;

    private String topic;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "topic_user",
            joinColumns = {@JoinColumn(name = "topic_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private List<User> users = new ArrayList<>();

    public Topic( String topic) {
        this.topic = topic;
    }

    public long getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(long topic_id) {
        this.topic_id = topic_id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
