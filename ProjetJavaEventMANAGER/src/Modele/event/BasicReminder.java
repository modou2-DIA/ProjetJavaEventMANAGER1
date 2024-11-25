/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modele.event;

import java.time.LocalDateTime;

/**
 *
 * @author nadae
 */
public class BasicReminder implements EventReminder {
    private LocalDateTime reminderTime;

    public BasicReminder(LocalDateTime reminderTime) {
        this.reminderTime = reminderTime;
    }

    @Override
    public void sendReminder() {
        System.out.println("Reminder sent for event at: " + reminderTime);
    }

    public LocalDateTime getReminderTime() {
        return reminderTime;
    }
}
