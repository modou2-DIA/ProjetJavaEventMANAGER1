/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modele.Reservations;
import java.util.Date;
public final class PendingReservation extends Reservation {
    private Date reminderDate;

    public PendingReservation() {
    }

    
    // Constructor now uses int for event ID instead of AbstractEvent
    public PendingReservation(int id, int id_event, int id_client, Date reminderDate) {
        super(id, id_event, id_client);  // Pass event ID to the super class constructor
        this.reminderDate = reminderDate;
    }

    public Date getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
    }

     

     

    @Override
    public String toString() {
        return super.toString() + ", ReminderDate=" + reminderDate;
    }
}


