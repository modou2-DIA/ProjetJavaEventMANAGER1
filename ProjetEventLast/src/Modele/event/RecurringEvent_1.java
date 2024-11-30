/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modele.Event;

/**
 *
 * @author DELL
 */

import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public final class RecurringEvent_1 extends AbstractEvent {
    private  String recurrencePattern;
    private  String recurrence_period ;
    private  LocalDateTime end_date ;

    public RecurringEvent_1() {
        
        this.recurrence_period = " ";
    }
     
    
     
    
    public RecurringEvent_1(String recurrencePattern, String recurrence_period, LocalDateTime end_date, String description, int id, String title, LocalDateTime date, String location,int isRecurring) {
        super(id, title, date, location,description,isRecurring);
        this.recurrencePattern = recurrencePattern;
        this.recurrence_period = recurrence_period;
        this.end_date = end_date;
         
        
    }

    public RecurringEvent_1(String recurrencePattern, String recurrence_period, LocalDateTime end_date, int id, String title, LocalDateTime date, String location, String description, int isRecurring, int idCategory) {
        super(id, title, date, location, description, isRecurring, idCategory);
        this.recurrencePattern = recurrencePattern;
        this.recurrence_period = recurrence_period;
        this.end_date = end_date;
    }
    
    public void setRecurrencePattern(String recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
    }

    public void setRecurrence_period(String recurrence_period) {
        this.recurrence_period = recurrence_period;
    }
    
     public void setEnd_date(String dateTimeStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            this.end_date  = LocalDateTime.parse(dateTimeStr, formatter);
        } catch (Exception e) {
            System.err.println("Erreur de format de date : " + e.getMessage());
            this.end_date = null;  // Ou gérer différemment si nécessaire
        }
    }
    public void setEnd_date(LocalDateTime end_date) {
        this.end_date = end_date;
    }
    

    public String getRecurrence_period() {
        return recurrence_period;
    }

    public LocalDateTime getEnd_date() {
        return end_date;
    }

    @Override
    public String getDescription() {
        return description;
    }
    
    
   public LocalDateTime getNextOccurrence(LocalDateTime lastOccurrence) {
    try {
        // Convertir la chaîne recurrence_period en un objet Period
        Period period = Period.parse(recurrence_period);

        // Ajouter la période pour calculer la prochaine occurrence
        return lastOccurrence.plus(period);
    } catch (DateTimeParseException e) {
        // Gérer le cas où recurrence_period n'est pas valide
        System.err.println("Erreur : recurrence_period invalide - " + recurrence_period);
        throw new IllegalArgumentException("Période de récurrence invalide : " + recurrence_period, e);
    }
   }
    public String getRecurrencePattern() {
        return recurrencePattern;
    }
        public List<LocalDateTime> calculateOccurrences() {
        List<LocalDateTime> occurrences = new ArrayList<>();
        LocalDateTime current = getDate();
        while (current.isBefore(end_date)) {
            occurrences.add(current);
            current = getNextOccurrence(current);
        }
        return occurrences;
    }

    @Override
    public void setIsRecurring(int isRecurring) {
        this.isRecurring = isRecurring;
    }
        

    @Override
    public String toString() {
        return super.toString() + "RecurringEvent{" + "recurrencePattern=" + recurrencePattern + ", recurrence_period=" + recurrence_period + ", end_date=" + end_date + ", description=" + description + '}';
    }

    
}

