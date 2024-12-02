/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modele.Reservations;
import java.time.LocalDateTime;
public class PendingReservation extends Reservation {
    private LocalDateTime reminderDate;

    // Constructor now uses int for event ID instead of AbstractEvent
    public PendingReservation(int id, int id_event, int id_client, LocalDateTime reminderDate) {
        super(id, id_event, id_client);  // Pass event ID to the super class constructor
        this.reminderDate = reminderDate;
    }

    public LocalDateTime getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(LocalDateTime reminderDate) {
        this.reminderDate = reminderDate;
    }

    public boolean needsReminder() {
        return reminderDate != null && reminderDate.isBefore(LocalDateTime.now());
    }

    public String generateReminder() {
        if (needsReminder()) {
            return "Reminder: Your reservation is coming up!";
        } else {
            return "No reminder needed.";
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", ReminderDate=" + reminderDate;
    }
}


