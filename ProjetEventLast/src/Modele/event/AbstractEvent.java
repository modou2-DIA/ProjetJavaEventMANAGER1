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
import java.time.format.DateTimeFormatter;

public sealed abstract class AbstractEvent permits BasicEvent_1, RecurringEvent_1  {
    protected   int id;
    protected   String title;
    protected  LocalDateTime date;
    protected   String location;
    protected   String description; 
    protected   int isRecurring; 
    protected int idCategory ; 

    public AbstractEvent() {
        this.isRecurring =0;
    }

    

    public AbstractEvent(int id, String title, LocalDateTime date, String location, String description,int isRecurring) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.location = location;
        this.description = description;
        this.isRecurring= isRecurring;
    }

    public AbstractEvent(int id, String title, LocalDateTime date, String location, String description, int isRecurring, int idCategory) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.location = location;
        this.description = description;
        this.isRecurring = isRecurring;
        this.idCategory = idCategory;
    }
    
    

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getDate() {
        
        return date;
    }
    public String getDates()
    {
                

        LocalDateTime dateTime = date ;

        // Définir un format de date (par exemple, "yyyy-MM-dd HH:mm:ss")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Convertir LocalDateTime en String
        String formattedDateTime = dateTime.format(formatter);

        return formattedDateTime;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }
    
    public void setDate(String dateTimeStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            this.date  = LocalDateTime.parse(dateTimeStr, formatter);
        } catch (Exception e) {
            System.err.println("Erreur de format de date : " + e.getMessage());
            this.date = null;  // Ou gérer différemment si nécessaire
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIsRecurring(int isRecurring) {
        this.isRecurring = isRecurring;
    }

    public int getIsRecurring() {
        return isRecurring;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
    
    
    
    
    

    @Override
    public String toString() {
        return "AbstractEvent{"  + ", title=" + title + ", date=" + date + ", location=" + location + ", description=" + description + '}';
    }
    
    
}

