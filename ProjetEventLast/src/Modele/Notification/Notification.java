/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modele.Notification;
import java.time.LocalDateTime;
/**
 *
 * @author nadae
 */
import java.time.LocalDateTime;

public sealed class Notification permits RecurringNotification, UrgentNotification {
    
    protected int id;
    protected String message;
    protected LocalDateTime sendDate=LocalDateTime.now();
    protected boolean isUrgent;

    public Notification() {}

    public Notification(int id, String message, boolean isUrgent) {
        this.id = id;
        this.message = message;
        this.isUrgent = isUrgent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getSendDate() {
        return sendDate;
    }


    public boolean isUrgent() {
        return isUrgent;
    }

    public void setUrgent(boolean isUrgent) {
        this.isUrgent = isUrgent;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", sendDate=" + sendDate +
                ", isUrgent=" + isUrgent +
                '}';
    }
}

