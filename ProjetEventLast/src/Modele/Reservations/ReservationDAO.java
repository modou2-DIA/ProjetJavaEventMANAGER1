/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modele.Reservations;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    private final Connection connection;

    // Constructeur
    public ReservationDAO(Connection connection) {
        this.connection = connection;
    }

    // Charger toutes les réservations depuis la base de données
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM Reservation";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Reservation reservation ;
                 int isConfirm =  resultSet.getInt("isConfirmed");
                 if(isConfirm==1)
                 {
                     reservation =  new ConfirmedReservation(
                        resultSet.getInt("id"),
                        resultSet.getInt("id_event"),
                        resultSet.getInt("client_id"),
                        resultSet.getString("confirmation_code")
                );
                     reservation.setIsConfirmed(isConfirm);
                 }
                 else
                 {
                     reservation =  new PendingReservation(
                        resultSet.getInt("id"),
                        resultSet.getInt("id_event"),
                        resultSet.getInt("client_id"),
                        resultSet.getTimestamp("reminder_date") != null ?
                                resultSet.getTimestamp("reminder_date").toLocalDateTime() : null
                            );
                     
                 }

                reservations.add(reservation);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des réservations : " + e.getMessage());
        }

        return reservations;
    }

    // Ajouter une nouvelle réservation
    public void addReservation(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO Reservation (id, id_event, client_id, isConfirmed, confirmation_code, reminder_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reservation.getId());
           statement.setInt(2, reservation.getIdEvent());
            statement.setInt(3, reservation.getId_client());
            statement.setInt(4, reservation.isConfirmed());
           
             if (reservation instanceof ConfirmedReservation) {
                 statement.setString(5, ((ConfirmedReservation) reservation).getConfirmationCode());
                 statement.setNull(6, Types.TIMESTAMP);
            } else if(reservation instanceof PendingReservation){
                statement.setString(5," ");
               statement.setTimestamp(6, Timestamp.valueOf(((PendingReservation) reservation).getReminderDate()));
            }
            statement.executeUpdate();
        }
    }

    // Modifier une réservation existante
    public void updateReservation(Reservation reservation) throws SQLException {
        String sql = "UPDATE Reservation SET id_event = ?, client_id = ?, isConfirmed = ?, " +
                     "confirmation_code = ?, reminder_date = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reservation.getIdEvent());
            statement.setInt(2, reservation.getId_client());
            statement.setInt(3, reservation.isConfirmed());
           
            
             if (reservation instanceof ConfirmedReservation) {
                 statement.setString(4, ((ConfirmedReservation) reservation).getConfirmationCode());
                 statement.setNull(5, Types.TIMESTAMP);
            } else if(reservation instanceof PendingReservation){
                statement.setString(4," ");
               statement.setTimestamp(5, Timestamp.valueOf(((PendingReservation) reservation).getReminderDate()));
            }

     

            statement.setInt(6, reservation.getId());
            statement.executeUpdate();
        }
    }

    // Supprimer une réservation par ID
    public void deleteReservation(int reservationId) throws SQLException {
        String sql = "DELETE FROM Reservation WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reservationId);
            statement.executeUpdate();
        }
    }

    // Rechercher des réservations par client ID
    public List<Reservation> getReservationsByClientId(int clientId) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM Reservation WHERE client_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clientId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                 Reservation reservation ;
                 int isConfirm =  resultSet.getInt("isConfirmed");
                 if(isConfirm==1)
                 {
                     reservation =  new ConfirmedReservation(
                        resultSet.getInt("id"),
                        resultSet.getInt("id_event"),
                        resultSet.getInt("client_id"),
                        resultSet.getString("confirmation_code")
                );
                     reservation.setIsConfirmed(isConfirm);
                 }
                 else
                 {
                     reservation =  new PendingReservation(
                        resultSet.getInt("id"),
                        resultSet.getInt("id_event"),
                        resultSet.getInt("client_id"),
                        resultSet.getTimestamp("reminder_date") != null ?
                                resultSet.getTimestamp("reminder_date").toLocalDateTime() : null
                            );
                     
                 }

                reservations.add(reservation);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des réservations : " + e.getMessage());
        }

        return reservations;
    }
}
