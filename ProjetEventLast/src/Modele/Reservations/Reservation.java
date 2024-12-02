/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modele.Reservations;
public class Reservation {
    private int id;
    private int id_event;  
    private int id_client;
    private int isConfirmed;

    
    public Reservation(int id, int id_event, int client) {
        this.id = id;
        this.id_event = id_event;  // Set the event ID directly
        this.id_client = client;
        this.isConfirmed = 0;  // Default to not confirmed
    }

    public int getId() {
        return id;
    }

    public int getId_event() {
        return id_event;
    }

    public int getId_client() {
        return id_client;
    }

    public int isIsConfirmed() {
        return isConfirmed;
    }

    public void setId_event(int id_event) {
        this.id_event = id_event;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public void setIsConfirmed(int isConfirmed) {
        this.isConfirmed = isConfirmed;
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

   
    public int isConfirmed() {
        return isConfirmed;
    }

    public void confirm() {
        this.isConfirmed = 1;  // Mark as confirmed
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", id_event=" + id_event +  // Display event ID
                ", client=" + id_client +
                ", isConfirmed=" + isConfirmed +
                '}';
    }
}


