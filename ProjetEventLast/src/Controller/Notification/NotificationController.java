/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller.Notification;

import Modele.Notification.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class NotificationController {
    private final NotificationDAO notificationDAO;

    public NotificationController(Connection connection) {
        this.notificationDAO = new NotificationDAO(connection);
    }

    public void addNotification(Notification notification) throws SQLException {
        notificationDAO.addNotification(notification);
    }

    public List<Notification> getAllNotifications() throws SQLException {
        return notificationDAO.getAllNotifications();
    }
}
