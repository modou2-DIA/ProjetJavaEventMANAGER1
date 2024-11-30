/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modele.Event;

import java.time.LocalDateTime;

/**
 *
 * @author DELL
 */


public final class BasicEvent_1 extends AbstractEvent {

    public BasicEvent_1() {
    }
    
    public BasicEvent_1(int id, String title, LocalDateTime date, String location,String description,int isRecurring) {
        super(id, title, date, location,description,isRecurring);
    }

    public BasicEvent_1(int id, String title, LocalDateTime date, String location, String description, int isRecurring, int idCategory) {
        super(id, title, date, location, description, isRecurring, idCategory);
    }
    
    
}

