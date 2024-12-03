package Vue.Reservation;

import Controller.Event.EventController;
import Modele.Reservations.Reservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AjouterReservationView {

    private final Stage stage;
    private final EventController controller;

    public AjouterReservationView(Stage stage, EventController controller) {
        this.stage = stage;
        this.controller = controller;
    }

    public void show(Reservation reservation) {
        String titleText = (reservation == null) ? "Ajouter une réservation" : "Modifier la réservation";
        Label titleLabel = new Label(titleText);
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // ComboBox pour les événements
        Label eventLabel = new Label("Événement :");
        ComboBox<String> eventComboBox = new ComboBox<>();
        ObservableList<String> eventOptions = FXCollections.observableArrayList(controller.getAllEventTitles());
        eventComboBox.setItems(eventOptions);
        eventComboBox.setPromptText("Choisir un événement");

        // ComboBox pour les clients
        Label clientLabel = new Label("Client :");
        ComboBox<String> clientComboBox = new ComboBox<>();
        ObservableList<String> clientOptions = FXCollections.observableArrayList(controller.getAllClientNames());
        clientComboBox.setItems(clientOptions);
        clientComboBox.setPromptText("Choisir un client");

        // Bouton pour ajouter un nouveau client
        Button addClientButton = new Button("Ajouter Client");
        addClientButton.setOnAction(e -> openAddClientDialog(clientComboBox));

        // Confirmation (Oui/Non)
        Label isConfirmedLabel = new Label("Confirmation (Oui/Non) :");
        ComboBox<String> isConfirmedComboBox = new ComboBox<>();
        isConfirmedComboBox.setItems(FXCollections.observableArrayList("Oui", "Non"));
        isConfirmedComboBox.setPromptText("Confirmer la réservation");

        if (reservation != null) {
            eventComboBox.setValue(controller.getEventTitle(reservation.getId_event()));
            clientComboBox.setValue(controller.getClientNameById(reservation.getId_client()));
            isConfirmedComboBox.setValue(reservation.isIsConfirmed() == 1 ? "Oui" : "Non");
        }

        Button saveButton = new Button((reservation == null) ? "Ajouter" : "Modifier");
        saveButton.setOnAction(e -> {
            try {
                if (eventComboBox.getValue() == null || clientComboBox.getValue() == null || isConfirmedComboBox.getValue() == null) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
                    return;
                }

                int eventId = controller.getEventIdByTitle(eventComboBox.getValue());
                int clientId = controller.getClientIdByName(clientComboBox.getValue());
                int isConfirmed = isConfirmedComboBox.getValue().equals("Oui") ? 1 : 0;

                if (reservation == null) {
                    Reservation newReservation = new Reservation(0, eventId, clientId);
                    newReservation.setIsConfirmed(isConfirmed);
                    controller.addReservation(newReservation);
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Réservation ajoutée avec succès !");
                } else {
                    reservation.setId_event(eventId);
                    reservation.setId_client(clientId);
                    reservation.setIsConfirmed(isConfirmed);
                    controller.updateReservation(reservation);
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Réservation modifiée avec succès !");
                }

                new GestionReservationsView(stage, controller).show();

            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue : " + ex.getMessage());
            }
        });

        Button cancelButton = new Button("Annuler");
        cancelButton.setOnAction(e -> new GestionReservationsView(stage, controller).show());

        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        HBox clientBox = new HBox(10, clientComboBox, addClientButton);
        clientBox.setAlignment(Pos.CENTER_LEFT);

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));
        grid.add(eventLabel, 0, 0);
        grid.add(eventComboBox, 1, 0);
        grid.add(clientLabel, 0, 1);
        grid.add(clientBox, 1, 1);
        grid.add(isConfirmedLabel, 0, 2);
        grid.add(isConfirmedComboBox, 1, 2);

        VBox layout = new VBox(20, titleLabel, grid, buttonBox);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 1000, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void openAddClientDialog(ComboBox<String> clientComboBox) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Ajouter un Client");

        Label nameLabel = new Label("Nom complet :");
        TextField nameField = new TextField();
        Label emailLabel = new Label("Email :");
        TextField emailField = new TextField();

        Button saveButton = new Button("Ajouter");
        saveButton.setOnAction(e -> {
            try {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                if (name.isEmpty() || email.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis.");
                    return;
                }

                boolean re=controller.addClient(name, email); // Appel à la méthode du contrôleur pour ajouter un client
                if(re)
                {
                    clientComboBox.getItems().add(name);
                    dialog.close();
                }
                else
                {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l ajout du client: " );
                }
                    
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue : " + ex.getMessage());
            }
        });

        VBox dialogLayout = new VBox(10, nameLabel, nameField, emailLabel, emailField, saveButton);
        dialogLayout.setPadding(new Insets(20));
        dialogLayout.setAlignment(Pos.CENTER);

        Scene dialogScene = new Scene(dialogLayout, 400, 200);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
