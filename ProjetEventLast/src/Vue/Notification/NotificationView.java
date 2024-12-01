/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author DELL
 */
package Vue.Notification;

import Controller.Notification.NotificationController;
import Modele.Notification.Notification;
import Modele.Notification.UrgentNotification;
import Modele.Notification.RecurringNotification;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;

public class NotificationView {
    private final Stage stage;
    private final NotificationController controller;
    private TableView<Notification> tableView;
    private ObservableList<Notification> notificationData;

    public NotificationView(Stage stage, NotificationController controller) {
        this.stage = stage;
        this.controller = controller;
        this.notificationData = FXCollections.observableArrayList();
    }

    public void show() {
         
        
        Label title = new Label("Gestion des Notifications");
         
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: #0078D7; -fx-font-weight: bold;");
        title.setAlignment(Pos.CENTER);
        // TableView
        tableView = new TableView<>();
        TableColumn<Notification, String> messageColumn = new TableColumn<>("Message");
        messageColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMessage()));

        TableColumn<Notification, String> dateColumn = new TableColumn<>("Date d'envoi");
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSendDate().toString()));

        TableColumn<Notification, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(cellData -> {
         int isUrgent = cellData.getValue().getIsUrgent();
            return new SimpleStringProperty(isUrgent == 1 ? "Urgent" : "Récurrent");
        }); 

        tableView.getColumns().addAll(messageColumn, dateColumn, typeColumn);

        // Charger les données
        loadNotifications();

        // Boutons
        Button addButton = new Button("Ajouter Notification");
        addButton.setOnAction(e -> showAddNotificationDialog());

        VBox layout = new VBox(20,title, tableView, addButton);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void loadNotifications() {
        try {
            notificationData.clear();
            notificationData.addAll(controller.getAllNotifications());
            tableView.setItems(notificationData);
        } catch (SQLException e) {
        }
    }

    private void showAddNotificationDialog() {
        // Ajouter une fenêtre pour créer une nouvelle notification
        // Implémenter selon vos besoins
    }
}

