package com.example.demo.model;

import java.util.List;

public class MessageEmail {

    private String topic;
    private String message;
    private String sender;
    private List<String> addressee;

    public MessageEmail() {}

    public MessageEmail(String topic, String message, String sender, List<String> addressee) {
        this.topic = topic;
        this.message = message;
        this.sender = sender;
        this.addressee = addressee;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<String> getAddressee() {
        return addressee;
    }

    public void setAddressee(List<String> addressee) {
        this.addressee = addressee;
    }
}

