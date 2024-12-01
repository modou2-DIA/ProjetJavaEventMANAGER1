/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modele.Reservation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public record ClientReservationStatus(
    int clientId,
    String clientName,
    int reservationId,
    String eventTitle,
    boolean isConfirmed
) {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";  
    private static final String USER = "projetjava";  
    private static final String PASSWORD = "123456"; 

    public static ClientReservationStatus fromReservation(Reservation reservation) {
        
        String eventTitle = getEventTitleById(reservation.getIdEvent());  // Fetch event title by ID
        
        return new ClientReservationStatus(
            reservation.getClient().getId(),
            reservation.getClient().getfullName(),  
            reservation.getId(),
            eventTitle,
            reservation.isConfirmed()
        );
    }

    // Example method to fetch event title by event ID (id_event)
    private static String getEventTitleById(int eventId) {
        String eventTitle = null;
        String query = "SELECT title FROM events WHERE id = ?"; 
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, eventId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                eventTitle = rs.getString("title");
            }
        } catch (SQLException e) {
        }
        return eventTitle;
    }
}

 
    

