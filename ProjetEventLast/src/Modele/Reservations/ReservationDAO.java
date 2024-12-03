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
public List<Reservation> getAllReservations() throws SQLException {
    List<Reservation> reservations = new ArrayList<>();
    String sql = "SELECT r.ID, r.ID_EVENT, r.CLIENT_ID, r.ISCONFIRMED, r.CONFIRMATION_CODE, r.REMINDER_DATE, " +
                 "c.FULLNAME AS CLIENT_NAME, e.TITLE AS EVENT_TITLE " +
                 "FROM RESERVATION r " +
                 "JOIN CLIENT c ON r.CLIENT_ID = c.ID " +
                 "JOIN EVENTS e ON r.ID_EVENT = e.ID";
    try (Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        while (rs.next()) {
            Reservation res;
            
            // Vérifier si la réservation est confirmée
            if (rs.getInt("ISCONFIRMED") == 1) {
                // Créer une instance de ConfirmedReservation
                ConfirmedReservation cres = new ConfirmedReservation();
                cres.setConfirmationCode(rs.getString("CONFIRMATION_CODE"));
                res = cres;
            } else {
                // Créer une instance de PendingReservation
                PendingReservation penres = new PendingReservation();
                penres.setReminderDate(rs.getDate("REMINDER_DATE"));
                res = penres;
            }
            
            // Remplir les champs communs
            res.setId(rs.getInt("ID"));
            res.setId_event(rs.getInt("ID_EVENT"));
            res.setId_client(rs.getInt("CLIENT_ID"));
            res.setIsConfirmed(rs.getInt("ISCONFIRMED"));
            res.setClient_name(rs.getString("CLIENT_NAME")); // Nouveau champ
            res.setTitle(rs.getString("EVENT_TITLE"));       // Nouveau champ
            
            reservations.add(res);
        }
    }
    return reservations;
}


    // Ajouter une nouvelle réservation
 public void addReservation(Reservation reservation) throws SQLException {
    String sql = "INSERT INTO Reservation (id, id_event, client_id, isConfirmed, confirmation_code, reminder_date) " +
                 "VALUES (?, ?, ?, ?, ?, ?)";

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
        // Définir les paramètres de la requête
        statement.setInt(1, reservation.getId());
        statement.setInt(2, reservation.getIdEvent());
        statement.setInt(3, reservation.getId_client());
        statement.setInt(4, reservation.isConfirmed());

        // Gestion des types spécifiques de réservation
        if (reservation instanceof ConfirmedReservation) {
            ConfirmedReservation confirmed = (ConfirmedReservation) reservation;
            statement.setString(5, confirmed.getConfirmationCode()); // Code de confirmation
            statement.setNull(6, Types.DATE); // Pas de date de rappel
        } else if (reservation instanceof PendingReservation) {
            PendingReservation pending = (PendingReservation) reservation;
            statement.setNull(5, Types.VARCHAR); // Pas de code de confirmation
            statement.setDate(6, (Date) pending.getReminderDate()); // Date de rappel
        } else {
            // Si la réservation n'est ni confirmée ni en attente
            statement.setNull(5, Types.VARCHAR);
            statement.setNull(6, Types.DATE);
        }

        // Exécuter la requête
        statement.executeUpdate();
    } catch (SQLException e) {
        // Gestion des erreurs SQL
        System.err.println("Erreur lors de l'ajout de la réservation : " + e.getMessage());
        throw e; // Relancer l'exception après journalisation
    }
}

// Modifier une réservation existante
public void updateReservation(Reservation reservation) throws SQLException {
    String sql = "UPDATE Reservation SET id_event = ?, client_id = ?, isConfirmed = ?, " +
                 "confirmation_code = ?, reminder_date = ? WHERE id = ?";

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
        // Paramètres communs
        statement.setInt(1, reservation.getIdEvent());
        statement.setInt(2, reservation.getId_client());
        statement.setInt(3, reservation.isConfirmed());

        // Gestion des types spécifiques de réservation
        if (reservation instanceof ConfirmedReservation) {
            ConfirmedReservation confirmed = (ConfirmedReservation) reservation;
            statement.setString(4, confirmed.getConfirmationCode()); // Code de confirmation
            statement.setNull(5, Types.TIMESTAMP); // Pas de date de rappel
        } else if (reservation instanceof PendingReservation) {
            PendingReservation pending = (PendingReservation) reservation;
            statement.setNull(4, Types.VARCHAR); // Pas de code de confirmation
            statement.setTimestamp(5, Timestamp.valueOf(pending.getReminderDate().toString())); // Date de rappel
        } else {
            // Si la réservation n'est ni confirmée ni en attente
            statement.setNull(4, Types.VARCHAR);
            statement.setNull(5, Types.TIMESTAMP);
        }

        // Identifiant de la réservation
        statement.setInt(6, reservation.getId());

        // Exécuter la requête
        statement.executeUpdate();
    } catch (SQLException e) {
        // Gestion des erreurs SQL
        System.err.println("Erreur lors de la mise à jour de la réservation : " + e.getMessage());
        throw e; // Relancer l'exception pour une gestion globale
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
                                resultSet.getTimestamp("reminder_date") : null
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
