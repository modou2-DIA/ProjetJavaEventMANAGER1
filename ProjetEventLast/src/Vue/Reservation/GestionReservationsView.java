package Vue.Reservation;

import Controller.Event.EventController;
import Modele.Reservations.Reservation;
import Vue.Event.GestionCategorieView;
import Vue.Event.GestionEvenementsView;
import Vue.Notification.NotificationView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import projetjavagestioneventement.HomePageView1;

public class GestionReservationsView {

    private final EventController controller;
    private final Stage stage;
    private TableView<Reservation> tableView;
    private ObservableList<Reservation> reservationData;
    private boolean isMenuCollapsed;
    private Button toggleMenuButton;

    public GestionReservationsView(Stage stage, EventController controller) {
        this.stage = stage;
        this.controller = controller;
        this.reservationData = FXCollections.observableArrayList();
        this.isMenuCollapsed = false;
    }

    public void show() {
        // *** MENU VERTICAL ***
        VBox menu = new VBox(15);
        menu.setPadding(new Insets(20));
        menu.setStyle("-fx-background-color: #2c3e50;");
        menu.setPrefWidth(200);

        toggleMenuButton = new Button("☰");
        toggleMenuButton.setStyle("-fx-background-color: #16a085; -fx-text-fill: white; -fx-font-size: 14px;");
        toggleMenuButton.setOnAction(e -> toggleMenu(menu));

        Button homeButton = createMenuButton("Accueil");
        Button eventButton = createMenuButton("Gestion des événements");
        Button reservationButton = createMenuButton("Gestion des réservations");
        Button notificationButton = createMenuButton("Gestion des notifications");
        Button categoryButton = createMenuButton("Gestion des catégories");

        menu.getChildren().addAll(toggleMenuButton, homeButton, eventButton, reservationButton, notificationButton, categoryButton);

        // Actions des boutons du menu
        reservationButton.setOnAction(e -> new GestionReservationsView(stage, controller).show());
        eventButton.setOnAction(e -> new GestionEvenementsView(stage, controller).show());
        homeButton.setOnAction(e -> new HomePageView1(stage, controller).show());
        categoryButton.setOnAction(e -> new GestionCategorieView(stage, controller).show());
        notificationButton.setOnAction(e -> new NotificationView(stage, controller).show());

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
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {
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
                    HBox actions = new HBox(5, editButton, deleteButton);
                    setGraphic(actions);
                }
            }
        });

        tableView.getColumns().addAll(clientColumn, eventColumn, statusColumn, actionsColumn);
        tableView.setItems(reservationData);
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

    private void toggleMenu(VBox menu) {
        if (isMenuCollapsed) {
            menu.setPrefWidth(200);
            menu.getChildren().forEach(node -> node.setVisible(true));
            toggleMenuButton.setText("☰");
        } else {
            menu.setPrefWidth(50);
            menu.getChildren().forEach(node -> node.setVisible(false));
            toggleMenuButton.setText("☰");
        }
        isMenuCollapsed = !isMenuCollapsed;
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;");
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }
}
