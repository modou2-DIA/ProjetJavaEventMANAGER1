/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vue.Event;

/**
 *
 * @author DELL
 */


import Controller.Event.EventController;
import Modele.Event.AbstractEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GestionEvenementsView {
    private final EventController controller;
    private final Stage stage;
    private TableView<AbstractEvent> tableView; // Rend tableView accessible globalement
    private ObservableList<AbstractEvent> eventData; // Données des événements

    public GestionEvenementsView(Stage stage, EventController controller) {
        this.stage = stage;
        this.controller = controller;
        this.eventData = FXCollections.observableArrayList(); // Initialisation de la liste observable
    }

    public void show() {
        
        
        // Titre
        Label title = new Label("Gestion des événements");
         
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: #0078D7; -fx-font-weight: bold;");
        title.setAlignment(Pos.CENTER);
        // Barre de recherche
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher par catégorie, titre, etc...");
        Button searchButton = new Button("Rechercher");

        HBox searchBar = new HBox(10, searchField, searchButton,resultArea);
        searchBar.setAlignment(Pos.CENTER);
        searchBar.setPadding(new Insets(10));
        
         searchButton.setOnAction(e -> {
            var results = controller.handleSearchEvents(searchField.getText());
            resultArea.setText(results.toString());
        });

        // TableView des événements
        tableView = new TableView<>();
        TableColumn<AbstractEvent, String> titleColumn = new TableColumn<>("Titre");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<AbstractEvent, String> locationColumn = new TableColumn<>("Lieu");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<AbstractEvent, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<AbstractEvent, Void> modifyColumn = new TableColumn<>("Modifier");
        modifyColumn.setCellFactory(param -> new TableCell<>() {
            private final Button modifyButton = new Button("Modifier");

            {
                modifyButton.setOnAction(e -> {
                    AbstractEvent event = getTableView().getItems().get(getIndex());
                    new AjouterEvenementView(stage, controller).show(event);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(modifyButton);
                }
            }
        });

        TableColumn<AbstractEvent, Void> deleteColumn = new TableColumn<>("Supprimer");
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(e -> {
                    AbstractEvent event = getTableView().getItems().get(getIndex());
                    controller.handleDeleteEvent(event); // Appel au contrôleur pour suppression
                    refreshTable(); // Rafraîchir la table
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        tableView.getColumns().addAll(titleColumn, locationColumn, dateColumn, modifyColumn, deleteColumn);
        // Lier les données de la table
        tableView.setItems(eventData);
        // Charger les données
        loadEventData();
        // Boutons principaux
        Button addEventButton = new Button("Ajouter événement");
        addEventButton.setOnAction(e -> new AjouterEvenementView(stage, controller).show(null));

        VBox layout = new VBox(20, title, searchBar, tableView, addEventButton);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    // Charger les données depuis le contrôleur
    private void loadEventData() {
        eventData.clear();
        eventData.addAll(controller.getAllEvents());
    }

    // Rafraîchir la table
    private void refreshTable() {
        loadEventData();
        tableView.refresh();
    }
}
