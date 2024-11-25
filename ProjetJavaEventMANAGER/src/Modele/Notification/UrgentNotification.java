/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modele.Notification;

/**
 *
 * @author nadae
 */

import java.time.LocalDateTime;


public final class UrgentNotification extends Notification implements NotificationSender {

    private String urgencyLevel;

    public UrgentNotification() {
        super(); 
    }

    public UrgentNotification(int id, String message, boolean isUrgent, String urgencyLevel) {
        super(id, message, isUrgent); // Call the parent class constructor
        this.urgencyLevel = urgencyLevel;
    }

    public String getUrgencyLevel() {
        return urgencyLevel;
    }

    public void setUrgencyLevel(String urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
    }

    @Override
    public void SendNotification() {
        System.out.println("URGENT: Sending notification with message: \"" + getMessage() + 
                           "\", urgency level: " + urgencyLevel + 
                           ", scheduled for: " + getSendDate());
    }
    @Override
    public String toString() {
        return "UrgentNotification{" +
               "id=" + id +
               ", message='" + message + '\'' +
               ", sendDate=" + sendDate +
               ", isUrgent=" + isUrgent +
               ", urgencyLevel='" + urgencyLevel + '\'' +
               '}';
    }
}

