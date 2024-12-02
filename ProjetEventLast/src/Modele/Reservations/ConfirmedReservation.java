/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modele.Reservations;

public final class ConfirmedReservation extends Reservation {
    private String confirmationCode;

    // Constructor now uses int for event ID instead of AbstractEvent
    public ConfirmedReservation(int id, int id_event, int id_client, String confirmationCode) {
        super(id, id_event, id_client);  // Pass event ID to the super class constructor
        this.confirmationCode = confirmationCode;
        confirm();  // Call confirm method from the parent class
    }

  
    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public boolean isPastReservation() {
        // Assuming you need a way to handle event details, you may need to query event information by event ID
        return true; // Placeholder: add logic to compare event date
    }

    public String getConfirmationDetails() {
        return "Reservation confirmed with code: " + confirmationCode;
    }

    @Override
    public String toString() {
        return super.toString() + ", ConfirmationCode='" + confirmationCode + "'";
    }
}

