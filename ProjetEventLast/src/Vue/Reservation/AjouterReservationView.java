package Vue.Reservation;

import Controller.Event.EventController;
import Modele.Reservations.Reservation;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AjouterReservationView {

    private final Stage stage;
    private final EventController controller;

    public AjouterReservationView(Stage stage, EventController controller) {
        this.stage = stage;
        this.controller = controller;
    }

    public void show(Reservation reservation) {
        // Titre de l'interface
        String titleText = (reservation == null) ? "Ajouter une réservation" : "Modifier la réservation";
        Label titleLabel = new Label(titleText);
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Champs de saisie
        Label eventIdLabel = new Label("ID de l'événement :");
        TextField eventIdField = new TextField();
        eventIdField.setPromptText("Entrez l'ID de l'événement");

        Label clientIdLabel = new Label("ID du client :");
        TextField clientIdField = new TextField();
        clientIdField.setPromptText("Entrez l'ID du client");

        Label isConfirmedLabel = new Label("Confirmation (1 = Oui, 0 = Non) :");
        TextField isConfirmedField = new TextField();
        isConfirmedField.setPromptText("Entrez 1 ou 0");

        // Pré-remplir les champs si une réservation est fournie (modification)
        if (reservation != null) {
            eventIdField.setText(String.valueOf(reservation.getId_event()));
            clientIdField.setText(String.valueOf(reservation.getId_client()));
            isConfirmedField.setText(String.valueOf(reservation.isIsConfirmed()));
        }

        // Bouton de sauvegarde
        Button saveButton = new Button((reservation == null) ? "Ajouter" : "Modifier");
        saveButton.setOnAction(e -> {
            try {
                int eventId = Integer.parseInt(eventIdField.getText());
                int clientId = Integer.parseInt(clientIdField.getText());
                int isConfirmed = Integer.parseInt(isConfirmedField.getText());

                if (reservation == null) {
                    // Ajouter une nouvelle réservation
                    Reservation newReservation = new Reservation(0, eventId, clientId);
                    newReservation.setIsConfirmed(isConfirmed);
                    controller.addReservation(newReservation);
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Réservation ajoutée avec succès !");
                } else {
                    // Modifier la réservation existante
                    reservation.setId_event(eventId);
                    reservation.setId_client(clientId);
                    reservation.setIsConfirmed(isConfirmed);
                    controller.updateReservation(reservation);
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Réservation modifiée avec succès !");
                }
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue : " + ex.getMessage());
            }
        });

        // Bouton d'annulation
        Button cancelButton = new Button("Annuler");
        cancelButton.setOnAction(e -> {
            // Retourner à la vue de gestion des réservations
            new GestionReservationsView(stage, controller).show();
        });

        // Mise en page
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));
        grid.add(eventIdLabel, 0, 0);
        grid.add(eventIdField, 1, 0);
        grid.add(clientIdLabel, 0, 1);
        grid.add(clientIdField, 1, 1);
        grid.add(isConfirmedLabel, 0, 2);
        grid.add(isConfirmedField, 1, 2);

        VBox layout = new VBox(20, titleLabel, grid, saveButton, cancelButton);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        // Scène
        Scene scene = new Scene(layout, 1000, 600);
        stage.setScene(scene);
        stage.show();
    }

    // Méthode utilitaire pour afficher des alertes
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
