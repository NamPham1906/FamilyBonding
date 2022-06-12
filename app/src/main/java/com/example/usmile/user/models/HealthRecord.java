package com.example.usmile.user.models;

import java.util.List;
import java.util.UUID;

public class HealthRecord {
    private String userID;
    private String description;
    private List<String> healthPictures;
    private String advices;
    private boolean deleted;
    private String sentDate;
    private String Description;
    private boolean accepted;

    public HealthRecord(String userID, String description, List<String> healthPictures, String advices, boolean deleted, String sentDate) {
        this.userID = userID;
        this.description = description;
        this.healthPictures = healthPictures;
        this.advices = advices;
        this.deleted = deleted;
        this.sentDate = sentDate;
    }

    public HealthRecord(String userID, String description, List<String> healthPictures, String advices, boolean accepted, boolean deleted, String sentDate) {
        this.userID = userID;
        this.description = description;
        this.healthPictures = healthPictures;
        this.advices = advices;
        this.deleted = deleted;
        this.sentDate = sentDate;
        this.accepted = accepted;
    }

    public HealthRecord() {

    }
    public String getUserID() { return userID;}

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getSentDate() {
        return sentDate;
    }

    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getHealthPictures() {
        return healthPictures;
    }

    public void setHealthPictures(List<String> healthPictures) {
        this.healthPictures = healthPictures;
    }

    public String getAdvices() {
        return advices;
    }

    public void setAdvices(String advices) {
        this.advices = advices;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
