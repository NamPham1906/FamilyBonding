package com.example.usmile.user.models;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class HealthRecord {
    private int ID;
    private List<String> healthPictures;
    private String advices;
    private boolean deleted;
    private String sentDate;

    public HealthRecord(int ID, List<String> healthPictures, String advices, boolean deleted, String sentDate) {
        this.ID = ID;
        this.healthPictures = healthPictures;
        this.advices = advices;
        this.deleted = deleted;
        this.sentDate = sentDate;
    }

    public HealthRecord() {

    }

    public String getSentDate() {
        return sentDate;
    }

    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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
