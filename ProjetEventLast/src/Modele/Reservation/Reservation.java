/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modele.Reservation;

import Modele.Event.AbstractEvent;
import Modele.client.Client;

/**
 *
 * @author nadae
 */
public class Reservation {
    private int id;
    private int id_event;  
    private Client client;
    private boolean isConfirmed;

    
    public Reservation(int id, int id_event, Client client) {
        this.id = id;
        this.id_event = id_event;  // Set the event ID directly
        this.client = client;
        this.isConfirmed = false;  // Default to not confirmed
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEvent() {
        return id_event;  // Getter for event ID
    }

    public void setIdEvent(int id_event) {
        this.id_event = id_event;  // Setter for event ID
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void confirm() {
        this.isConfirmed = true;  // Mark as confirmed
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", id_event=" + id_event +  // Display event ID
                ", client=" + client +
                ", isConfirmed=" + isConfirmed +
                '}';
    }
}


