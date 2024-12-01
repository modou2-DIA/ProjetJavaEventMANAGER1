/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modele.Notification;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {
    private final Connection connection;

    public NotificationDAO(Connection connection) {
        this.connection = connection;
    }

    // Ajouter une notification
    public void addNotification(Notification notification) throws SQLException {
        String sql = "INSERT INTO notifications (id, message, send_date, is_urgent, urgency_level, recurrence_interval) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, notification.getId());
            statement.setString(2, notification.getMessage());
            statement.setTimestamp(3, Timestamp.valueOf(notification.getSendDate()));
            statement.setInt(4, notification.getIsUrgent());
            
            switch (notification) {
                case UrgentNotification urgentNotification -> {
                    statement.setString(5, urgentNotification.getUrgencyLevel());
                    statement.setNull(6, Types.INTEGER);
                }
                case RecurringNotification recurringNotification -> {
                    statement.setNull(5, Types.VARCHAR);
                    statement.setInt(6, recurringNotification.getRecurrenceInterval());
                }
                default -> {
                    statement.setNull(5, Types.VARCHAR);
                    statement.setNull(6, Types.INTEGER);
                }
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement de l ajout de notification : " + e.getMessage());
        }
    }

    // Récupérer toutes les notifications
    public List<Notification> getAllNotifications() throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notifications";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String message = rs.getString("message");
                LocalDateTime sendDate = rs.getTimestamp("send_date").toLocalDateTime();
                int isUrgent = rs.getInt("is_urgent");

                if (isUrgent==1) {
                    String urgencyLevel = rs.getString("urgency_level");
                    notifications.add(new UrgentNotification(id, message, isUrgent, urgencyLevel));
                } else {
                    int recurrenceInterval = rs.getInt("recurrence_interval");
                    notifications.add(new RecurringNotification(id, message, isUrgent, recurrenceInterval));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des notifications : " + e.getMessage());
        }
        return notifications;
    }
}

