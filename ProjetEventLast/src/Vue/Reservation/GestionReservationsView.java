package Vue.Reservation;

import Controller.Event.EventController;
import Controller.Reservation.ReservationController;
import Modele.Reservations.Reservation;
import Vue.Event.GestionEvenementsView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import projetjavagestioneventement.MenuView;

public class GestionReservationsView {

    private final EventController controller;
    private final Stage stage;
    private TableView<Reservation> tableView;
    private ObservableList<Reservation> reservationData;
    private boolean isMenuCollapsed;
    
    

    public GestionReservationsView(Stage stage,EventController controller) {
        this.stage = stage;
      
        this.controller = controller;
        this.reservationData = FXCollections.observableArrayList();
        this.isMenuCollapsed = false;
    }

    public void show() {
        
         
        // *** MENU VERTICAL ***
        VBox menu = new MenuView(stage,controller,isMenuCollapsed ).menu();
       

        // *** Titre ***
        Label title = new Label("Gestion des Réservations");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: #0078D7; -fx-font-weight: bold;");
        title.setAlignment(Pos.CENTER);

        // *** Barre de recherche et ajout ***
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher par client, événement...");
        Button searchButton = new Button("Rechercher");
        Button addButton = new Button("+ Ajouter une réservation");

        HBox topBar = new HBox(10, searchField, searchButton, addButton);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.TOP_CENTER);

        searchButton.setOnAction(e -> refreshTable(searchField.getText()));
        addButton.setOnAction(e -> new AjouterReservationView(stage, controller).show(null));

        // *** TableView des réservations ***
        tableView = new TableView<>();

        // Colonne Client
        TableColumn<Reservation, String> clientColumn = new TableColumn<>("Client");
        clientColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getClient_name());
        });

        // Colonne Événement
        TableColumn<Reservation, String> eventColumn = new TableColumn<>("Événement");
        eventColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getTitle());
        });

        // Colonne Statut
        TableColumn<Reservation, String> statusColumn = new TableColumn<>("Statut");
        statusColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().isConfirmed() == 1 ? "Confirmée" : "Non confirmée");
        });

        // Colonne Actions
        TableColumn<Reservation, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = new Button();
            private final Button deleteButton = new Button();
             private final Button viewHistoryButton = new Button("Historique");

            {
                // Ajouter une icône pour le bouton Modifier
                 ImageView editIcon = new ImageView(new Image(GestionReservationsView .class.getResourceAsStream("/Images/edit.png")));
                if (editIcon.getImage() == null) {
                    throw new RuntimeException("Image resource not found: ../Images/edit.png");
                }
                
                editIcon.setFitWidth(16); // Largeur de l'icône
                editIcon.setFitHeight(16); // Hauteur de l'icône
                editButton.setGraphic(editIcon); // Définir l'icône sur le bouton
                editButton.setStyle("-fx-background-color: transparent;"); // Rendre le fond du bouton transparent

                // Ajouter une icône pour le bouton Supprimer
                ImageView deleteIcon = new ImageView(new Image(GestionReservationsView.class.getResourceAsStream("/Images/delete.png")));
                if (deleteIcon.getImage() == null) {
                    throw new RuntimeException("Image resource not found: /Images/delete.png");
                }

                
                deleteIcon.setFitWidth(16);
                deleteIcon.setFitHeight(16);
                deleteButton.setGraphic(deleteIcon);
                deleteButton.setStyle("-fx-background-color: transparent;");

                
                 viewHistoryButton.setOnAction(event -> {
                Reservation reservation = getTableView().getItems().get(getIndex());
                List<Reservation> clientHistory = controller.getReservationsByClientId(reservation.getId_client());
                displayClientHistory(clientHistory);
            });
                editButton.setOnAction(event -> {
                    Reservation reservation = getTableView().getItems().get(getIndex());
                    new AjouterReservationView(stage, controller).show(reservation);
                });
                deleteButton.setOnAction(event -> {
                    Reservation reservation = getTableView().getItems().get(getIndex());
                    Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationDialog.setTitle("Confirmation de suppression");
                    confirmationDialog.setHeaderText("Voulez-vous vraiment supprimer cette réservation ?");
                    confirmationDialog.setContentText("Client : " + reservation.getClient_name() + "\nÉvénement : " + reservation.getTitle());
                    confirmationDialog.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            try {
                                controller.deleteReservation(reservation.getId());
                                reservationData.remove(reservation);
                                refreshTable(null);
                            } catch (SQLException ex) {
                                Logger.getLogger(GestionReservationsView.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox actions = new HBox(5, editButton, deleteButton,viewHistoryButton);
                    setGraphic(actions);
                }
            }
        });

        tableView.getColumns().addAll(clientColumn, eventColumn, statusColumn, actionsColumn);
        tableView.setItems(reservationData);
        
         // *** Ajout du gestionnaire de survol ***
         
        tableView.setRowFactory(tv -> {
            TableRow<Reservation> row = new TableRow<>();
            row.setOnMousePressed(event -> {
                if (!row.isEmpty()) {
                    Reservation reservation = row.getItem();
                    showClientReservationHistory(reservation.getId_client());
                }
            });
            return row;
        });
        
        loadReservationData(null);

        // *** Mise en page ***
        VBox mainContent = new VBox(20, title, topBar, tableView);
        mainContent.setPadding(new Insets(20));
        mainContent.setAlignment(Pos.TOP_CENTER);

        BorderPane layout = new BorderPane();
        layout.setLeft(menu);
        layout.setCenter(mainContent);

        Scene scene = new Scene(layout, 1000, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void loadReservationData(String searchQuery) {
        reservationData.clear();
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            try {
                reservationData.addAll(controller.getAllReservations());
            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Échec de l'opération");
                alert.setContentText("Erreur lors du chargement des Reservation\n");
                alert.show();
            }
        } else {
            //reservationData.addAll(controller.searchReservations(searchQuery));
        }
    }

    private void refreshTable(String searchQuery) {
        loadReservationData(searchQuery);
        tableView.refresh();
    }


  
    private void showClientReservationHistory(int clientId) {
        String clientName = controller.getClientNameById(clientId);
        ObservableList<Reservation> clientReservations = FXCollections.observableArrayList(controller.getReservationsByClientId( clientId));
        Stage historyStage = new Stage();
        historyStage.setTitle("Historique des réservations pour " + clientName);
        TableView<Reservation> historyTable = new TableView<>(clientReservations);
        TableColumn<Reservation, String> eventColumn = new TableColumn<>("Événement");
        eventColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        TableColumn<Reservation, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateReservation()!=null?cellData.getValue().getDateReservation() .toString():" "));
        TableColumn<Reservation, String> statusColumn = new TableColumn<>("Statut");
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().isConfirmed() == 1 ? "Confirmée" : "Non confirmée"));
        historyTable.getColumns().addAll(eventColumn, dateColumn, statusColumn);
        VBox layout = new VBox(10, new Label("Historique des réservations :"), historyTable);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);
        Scene scene = new Scene(layout, 600, 400);
        historyStage.setScene(scene);
        historyStage.show();
    }
    
    // Méthode pour afficher l'historique des réservations
private void displayClientHistory(List<Reservation> reservations) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Historique du client");
    StringBuilder content = new StringBuilder("Historique des réservations :");
    int nbr=0;
    for (Reservation res : reservations) {
        if(nbr==0)
        {
            String clientName = controller.getClientNameById(res.getId_client());
            content.append("").append(clientName.toUpperCase()).append("\n\n\n");
        }
        nbr++;
        content.append(res.toString()).append("\n\n");
    }
    alert.setContentText(content.toString());
    alert.showAndWait();
}

}
