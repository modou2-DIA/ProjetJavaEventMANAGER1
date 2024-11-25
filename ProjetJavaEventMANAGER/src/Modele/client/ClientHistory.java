/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modele.client;

/**
 *
 * @author DELL
 */
import Modele.Event.AbstractEvent;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
//import Reservations.BasicReservation;
class EventNotFoundException extends Exception {
    public EventNotFoundException(String message) {
        super(message);
    }
}
public class ClientHistory {
    private List<AbstractEvent> events;
    //private List<BasicReservation> reservations;

    
    public ClientHistory() {
        this.events = new ArrayList<>();
        //this.reservations = new ArrayList<>();
    }

    
    public List<AbstractEvent> getEvents() {
        return events;
    }

    /*public List<BasicReservation> getReservations() {
        return reservations;
    }*/

    
    public void setEvents(List<AbstractEvent> events) {
        this.events = events;
    }

    /*public void setReservations(List<BasicReservation> reservations) {
        this.reservations = reservations;
    }*/


    public void addEvent(AbstractEvent event) {
        events.add(event);
    }

    /*public void addReservation(BasicReservation reservation) {
        reservations.add(reservation);
    }*/

    public void deleteEvent(AbstractEvent event) {
        events.remove(event);
    }

    /*public void deleteReservation(BasicReservation reservation) {
        reservations.remove(reservation);
    }*/

    public List<AbstractEvent> getRecentEvents() {
        int size = events.size();
        return events.subList(Math.max(0, size - 5), size);
    }
    public Optional<AbstractEvent> findEventByTitle(String title) {
        return events.stream()
                .filter(event -> event.getTitle().equalsIgnoreCase(title))
                .findFirst();
    }
    public void deleteEventByTitle(String title) throws EventNotFoundException {
        boolean found = false;

        for (AbstractEvent event : events) {
            if (event.getTitle().equalsIgnoreCase(title)) {
                events.remove(event);
                found = true;
                break; // Break after finding and removing the event
            }
        }

        if (!found) {
            throw new EventNotFoundException("Event with title '" + title + "' not found.");
        }
    }


    @Override
    public String toString() {
        return "ClientHistory{" +
                "events=" + events +
                //", reservations=" + reservations +
                '}';
    }
}

