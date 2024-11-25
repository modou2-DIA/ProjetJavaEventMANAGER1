/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modele.Notification;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
/**
 *
 * @author nadae
 */


public final class RecurringNotification extends Notification implements NotificationSender {

    
    private int recurrenceInterval; 
  
    public RecurringNotification() {
        super(); 
    }

    
    public RecurringNotification(int id, String message, boolean isUrgent, int recurrenceInterval) {
        super(id, message, isUrgent); 
        this.recurrenceInterval = recurrenceInterval;
    }

    
    public int getRecurrenceInterval() {
        return recurrenceInterval;
    }

    
    public void setRecurrenceInterval(int recurrenceInterval) {
        this.recurrenceInterval = recurrenceInterval;
    }

    
    public LocalDateTime getNextSendDate() {
        return getSendDate().plusDays(recurrenceInterval); 
    }

    
    public List<LocalDateTime> getFutureSendDates(int count) {
        return IntStream.range(1, count + 1) 
            .mapToObj(i -> getSendDate().plusDays(i * recurrenceInterval)) 
            .collect(Collectors.toList()); 
    }

    
    @Override
    public void SendNotification() {
        System.out.println("Recurring notification sent: " + getMessage() + 
                           ", next send date: " + getNextSendDate());
    }

    
    @Override
    public String toString() {
        return "RecurringNotification{" +
               "id=" + id +
               ", message='" + message + '\'' +
               ", sendDate=" + sendDate +
               ", isUrgent=" + isUrgent +
               ", recurrenceInterval=" + recurrenceInterval +
               '}';
    }
}

