package com.example.demo.model.error;

import java.util.Date;

public class ErrorMessage {

    private Date dateNow;
    private String title;
    private int status;
    private String message;

    public ErrorMessage(String title, int status, String message) {
        this.dateNow = new Date();
        this.title = title;
        this.status = status;
        this.message = message;
    }

    public Date getDateNow() {
        return dateNow;
    }

    public void setDateNow(Date dateNow) {
        this.dateNow = dateNow;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
