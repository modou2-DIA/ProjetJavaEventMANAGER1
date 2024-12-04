/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author DELL
 */
package Vue.Notification;

import Controller.Event.EventController;
import Modele.Notification.Notification;
import Modele.Notification.UrgentNotification;
import Modele.Notification.RecurringNotification;
import Vue.Event.GestionCategorieView;
import Vue.Event.GestionEvenementsView;
import Vue.Reservation.GestionReservationsView;

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
import projetjavagestioneventement.HomePageView1;
import projetjavagestioneventement.MenuView;

public class NotificationView {
    private final Stage stage;
    private final EventController controller;
    private TableView<Notification> tableView;
    private ObservableList<Notification> notificationData;
    
      private boolean isMenuCollapsed   ;
    private Button toggleMenuButton;

    public NotificationView(Stage stage, EventController controller) {
        this.stage = stage;
        this.controller = controller;
        this.notificationData = FXCollections.observableArrayList();
        isMenuCollapsed=false;
    }

    public void show() { 
        
         VBox menu = new MenuView(stage,controller,isMenuCollapsed ).menu();
         // *** MENU VERTICAL COLLAPSIBLE ***
        
        
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

        
         VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.setAlignment(Pos.TOP_CENTER);
        
        mainContent.getChildren().addAll(title, tableView, addButton);
        BorderPane layout = new BorderPane();
        layout.setLeft(menu);
        layout.setCenter(mainContent);


        Scene scene = new Scene(layout, 1000, 600);
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
    // Fenêtre de dialogue pour ajouter une notification
    Stage dialogStage = new Stage();
    dialogStage.setTitle("Ajouter une Notification");

    // Champs de saisie
    Label messageLabel = new Label("Message :");
    TextField messageField = new TextField();

    Label typeLabel = new Label("Type de Notification :");
    ToggleGroup typeGroup = new ToggleGroup();
    RadioButton urgentButton = new RadioButton("Urgente");
    RadioButton recurringButton = new RadioButton("Récurrente");
    urgentButton.setToggleGroup(typeGroup);
    recurringButton.setToggleGroup(typeGroup);

    // Champs spécifiques pour notification urgente
    Label urgencyLevelLabel = new Label("Niveau d'urgence :");
    TextField urgencyLevelField = new TextField();
    urgencyLevelField.setPromptText("Exemple : Haute, Moyenne, Faible");
    urgencyLevelField.setVisible(false);

    // Champs spécifiques pour notification récurrente
    Label recurrenceIntervalLabel = new Label("Intervalle de récurrence (jours) :");
    TextField recurrenceIntervalField = new TextField();
    recurrenceIntervalField.setPromptText("Exemple : 7 pour une semaine");
    recurrenceIntervalField.setVisible(false);

    // Afficher les champs en fonction du type sélectionné
    typeGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
        boolean isUrgentSelected = urgentButton.isSelected();
        urgencyLevelLabel.setVisible(isUrgentSelected);
        urgencyLevelField.setVisible(isUrgentSelected);
        recurrenceIntervalLabel.setVisible(!isUrgentSelected);
        recurrenceIntervalField.setVisible(!isUrgentSelected);
    });

        // Boutons
        Button saveButton = new Button("Enregistrer");
        saveButton.setOnAction(e -> {
            String message = messageField.getText();

        // Vérification des champs requis
        if (message.isEmpty() || typeGroup.getSelectedToggle() == null) {
            showErrorDialog("Veuillez remplir tous les champs requis !");
            return;
        }

        try {
            if (urgentButton.isSelected()) {
                String urgencyLevel = urgencyLevelField.getText();
                if (urgencyLevel.isEmpty()) {
                    showErrorDialog("Veuillez spécifier le niveau d'urgence !");
                    return;
                }

                // Créer une notification urgente
                UrgentNotification urgentNotification = new UrgentNotification(
                        0, message, 1, urgencyLevel
                );
                controller.addNotification(urgentNotification);
            } else if (recurringButton.isSelected()) {
                String recurrenceIntervalStr = recurrenceIntervalField.getText();
                if (recurrenceIntervalStr.isEmpty()) {
                    showErrorDialog("Veuillez spécifier l'intervalle de récurrence !");
                    return;
                }

                int recurrenceInterval;
                try {
                    recurrenceInterval = Integer.parseInt(recurrenceIntervalStr);
                } catch (NumberFormatException ex) {
                    showErrorDialog("L'intervalle de récurrence doit être un nombre !");
                    return;
                }

                // Créer une notification récurrente
                RecurringNotification recurringNotification = new RecurringNotification(
                        0, message, 0, recurrenceInterval
                );
                controller.addNotification(recurringNotification);
            }

            // Rafraîchir la table et fermer la boîte de dialogue
            loadNotifications();
            dialogStage.close();
            showSuccessDialog("Notification ajoutée avec succès !");
        } catch (SQLException ex) {
            showErrorDialog("Erreur lors de l'ajout de la notification !");
            
        }
    });

            Button cancelButton = new Button("Annuler");
            cancelButton.setOnAction(e -> dialogStage.close());

            // Mise en page
            VBox formLayout = new VBox(10,
            messageLabel, messageField,
            typeLabel, urgentButton, recurringButton,
            urgencyLevelLabel, urgencyLevelField,
            recurrenceIntervalLabel, recurrenceIntervalField,
            new HBox(10, saveButton, cancelButton)
            );
            formLayout.setPadding(new Insets(20));
            formLayout.setAlignment(Pos.CENTER);

            Scene dialogScene = new Scene(formLayout, 400, 400);
            dialogStage.setScene(dialogScene);
            dialogStage.show();
        }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    } 
    
    
    
}

