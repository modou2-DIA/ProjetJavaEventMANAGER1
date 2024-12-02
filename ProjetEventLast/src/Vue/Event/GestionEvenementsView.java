/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vue.Event;
import Controller.Event.EventController;
import Modele.Event.AbstractEvent;
import Vue.Notification.NotificationView;
import Vue.Reservation.GestionReservationsView;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import projetjavagestioneventement.HomePageView1;

public class GestionEvenementsView {
    private final EventController controller;
    private final Stage stage;
    private TableView<AbstractEvent> tableView; // Rend tableView accessible globalement
    private ObservableList<AbstractEvent> eventData; // Données des événements
    
    private boolean isMenuCollapsed   ;
    
    private Button toggleMenuButton;
    
    
    
    
    public GestionEvenementsView(Stage stage, EventController controller) {
        this.stage = stage;
        this.controller = controller;
        this.eventData = FXCollections.observableArrayList();// Initialisation de la liste observable
         isMenuCollapsed = false;
    }

    public void show() {
        
         // *** MENU VERTICAL COLLAPSIBLE ***
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
        
         reservationButton.setOnAction(e -> new GestionReservationsView(stage, controller).show());
        eventButton.setOnAction(e -> new GestionEvenementsView(stage,controller).show());
        
        homeButton.setOnAction(e -> new HomePageView1(stage,controller).show() );
        
        categoryButton.setOnAction(e -> new GestionCategorieView(stage,controller).show());
        
        notificationButton.setOnAction(e -> new NotificationView(stage,controller).show());
       
        // Titre
        Label title = new Label("Gestion des événements");
         
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: #0078D7; -fx-font-weight: bold;");
        title.setAlignment(Pos.CENTER);
        // Barre de recherche
        
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher par titre, categorie, etc...");
        Button searchButton = new Button("Rechercher");

        Button addButton = new Button("+ Ajouter un evenement");

        HBox topBar = new HBox(10, searchField, searchButton, addButton);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #f4f4f4;");
        topBar.setAlignment(Pos.TOP_CENTER);
        
        VBox head = new VBox(10,topBar);
        head.setPadding(new Insets(10));
       head.setStyle("-fx-background-color: #f4f4f4;");
       head.setAlignment(Pos.CENTER);
        head.setPadding(new Insets(10));
       
        searchButton.setOnAction(e -> {
             
                    refreshTable(searchField.getText() );
    
        });
        
        
        addButton.setOnAction(e -> new AjouterEvenementView(stage, controller).show());
    

        // TableView des événements
        tableView = new TableView<>();
        TableColumn<AbstractEvent, String> titleColumn = new TableColumn<>("Titre");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<AbstractEvent, String> locationColumn = new TableColumn<>("Lieu");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<AbstractEvent, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        TableColumn<AbstractEvent, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        
        TableColumn<AbstractEvent, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(cellData -> {
        Integer categoryId = cellData.getValue().getIdCategory(); // Récupérer l'idCategory
        String categoryName = " "; // Obtenir le nom de la catégorie
        try{
            categoryName =controller.getCategoryName(categoryId);
        }
        catch(SQLException ex)
        {
            
        }
        return new SimpleStringProperty(categoryName); // Retourner une propriété observable
    });

        
        TableColumn<AbstractEvent, String> recurringColumn = new TableColumn<>("Est_recurrent");
        recurringColumn .setCellValueFactory(new PropertyValueFactory<>("isRecurring"));

        TableColumn<AbstractEvent, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {
                editButton.setOnAction(event -> {
                    AbstractEvent evenenemnt = getTableView().getItems().get(getIndex());
                    new ModifierEvenementView(stage,controller,evenenemnt).show(evenenemnt);
                    
                });
                deleteButton.setOnAction(event -> {
                // Récupérer l'objet spectacle à partir de la table
                AbstractEvent evenenemnt = getTableView().getItems().get(getIndex());

                // Créer une boîte de dialogue de confirmation
                Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationDialog.setTitle("Confirmation de suppression");
                confirmationDialog.setHeaderText("Voulez-vous vraiment supprimer cet évenement ?");
                confirmationDialog.setContentText("Titre : " + evenenemnt.getTitle() + "\nID : " + evenenemnt.getId());

                // Ajouter les boutons Yes et No
                ButtonType yesButton = new ButtonType("Oui", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("Non", ButtonBar.ButtonData.NO);
                confirmationDialog.getButtonTypes().setAll(yesButton, noButton);

                // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
                confirmationDialog.showAndWait().ifPresent(response -> {
                    if (response == yesButton) {
                        try {
                            // Supprimer le spectacle si l'utilisateur a confirmé
                            
                            controller.handleDeleteEvent(evenenemnt);
                            
                            eventData.remove(evenenemnt);
                            
                            // Rafraîchir la table
                            refreshTable(null);
                        } catch (SQLException ex) {
                            Logger.getLogger(GestionEvenementsView.class.getName()).log(Level.SEVERE, null, ex);
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
                    setGraphic(actions);
                }
            }
        });         
         
        tableView.getColumns().addAll(titleColumn, locationColumn, dateColumn,descriptionColumn,categoryColumn,recurringColumn,actionsColumn);
        // Lier les données de la table
        tableView.setItems(eventData);
        // Charger les données
        loadEventData(null);
        // Boutons principaux
        
         VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.setAlignment(Pos.TOP_CENTER);
        
        mainContent.getChildren().addAll(title, head, tableView);
        BorderPane layout = new BorderPane();
        layout.setLeft(menu);
        layout.setCenter(mainContent);


        Scene scene = new Scene(layout, 1000, 600);
        stage.setScene(scene);
        stage.show();
    }
        
        
    // Charger les données depuis le contrôleur
    private void loadEventData(String mot) {
        eventData.clear();
        eventData.addAll(controller.getAllEvents(mot));
    }

    // Rafraîchir la table
    private void refreshTable(String mot) {
        loadEventData(mot);
        tableView.refresh();
    } 
    
     // Méthode pour rendre le menu rétractable
    // Méthode pour rendre le menu rétractable
    private void toggleMenu(VBox menu) {
        if (isMenuCollapsed) {
            menu.setPrefWidth(200);
            menu.getChildren().forEach(node -> node.setVisible(true));
            toggleMenuButton.setText("☰"); // Réinitialiser le texte du bouton
        } else {
            menu.setPrefWidth(50);
            menu.getChildren().forEach(node -> {
                if (node != toggleMenuButton) {
                    node.setVisible(false);
                }
            });
            toggleMenuButton.setText("☰"); // Maintenir le bouton visible
        }
        isMenuCollapsed = !isMenuCollapsed;
    }
     // Méthode pour créer un bouton de menu avec un style unifié
    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #16a085; -fx-text-fill: white;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;"));
        return button;
    }


       
 }


