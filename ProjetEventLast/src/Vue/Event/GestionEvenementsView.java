/*
package Vue.Event;
import Controller.Event.EventController;
import Modele.Event.AbstractEvent;
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

import projetjavagestioneventement.MenuView;

public class GestionEvenementsView {
    private final EventController controller;
    private final Stage stage;
    private TableView<AbstractEvent> tableView; // Rend tableView accessible globalement
    private ObservableList<AbstractEvent> eventData; // Données des événements
    
    private boolean isMenuCollapsed   ;

    public GestionEvenementsView(Stage stage, EventController controller) {
        this.stage = stage;
        this.controller = controller;
        this.eventData = FXCollections.observableArrayList();// Initialisation de la liste observable
         isMenuCollapsed = false;
    }

    public void show() {
        
         // *** MENU VERTICAL COLLAPSIBLE ***
          VBox menu = new MenuView(stage,controller,isMenuCollapsed ).menu();

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
    
   
       
 }
*/

package Vue.Event;

import Controller.Event.EventController;
import Modele.Event.AbstractEvent;
import Modele.Event.EventException;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import projetjavagestioneventement.MenuView;

public class GestionEvenementsView {
    private final EventController controller;
    private final Stage stage;
    private TableView<AbstractEvent> tableView; // Rend tableView accessible globalement
    private ObservableList<AbstractEvent> eventData; // Données des événements
    
    private boolean isMenuCollapsed;

    public GestionEvenementsView(Stage stage, EventController controller) {
        this.stage = stage;
        this.controller = controller;
        this.eventData = FXCollections.observableArrayList(); // Initialisation de la liste observable
        isMenuCollapsed = false;
    }

    public void show() {
        // *** MENU VERTICAL COLLAPSIBLE ***
        VBox menu = new MenuView(stage, controller, isMenuCollapsed).menu();

        // Titre
        Label title = new Label("Gestion des événements");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: #0078D7; -fx-font-weight: bold;");
        title.setAlignment(Pos.CENTER);

        // Barre de recherche
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher par titre, catégorie, etc...");
        Button searchButton = new Button("Rechercher");
        Button addButton = new Button("+ Ajouter un événement");

        HBox topBar = new HBox(10, searchField, searchButton, addButton);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #f4f4f4;");
        topBar.setAlignment(Pos.TOP_CENTER);

        VBox head = new VBox(10, topBar);
        head.setPadding(new Insets(10));
        head.setStyle("-fx-background-color: #f4f4f4;");
        head.setAlignment(Pos.CENTER);

        searchButton.setOnAction(e -> refreshTable(searchField.getText()));
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

        TableColumn<AbstractEvent, String> categoryColumn = new TableColumn<>("Catégorie");
        categoryColumn.setCellValueFactory(cellData -> {
            Integer categoryId = cellData.getValue().getIdCategory();
            try {
                return new SimpleStringProperty(controller.getCategoryName(categoryId));
            } catch (SQLException ex) {
                return new SimpleStringProperty("Inconnue");
            }
        });

        TableColumn<AbstractEvent, Void> colorColumn = new TableColumn<>("Code Couleur");
        colorColumn.setCellFactory(column -> new TableCell<>() {
            private final Rectangle colorIndicator = new Rectangle(10, 10);

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    AbstractEvent event = getTableRow().getItem();
                    try {
                        String colorCode = controller.getCategoryColor(event.getIdCategory());
                        colorIndicator.setFill(Color.web(colorCode));
                    } catch (SQLException ex) {
                        colorIndicator.setFill(Color.GRAY);
                    }
                    setGraphic(colorIndicator);
                }
            }
        });

        TableColumn<AbstractEvent, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = new Button();
            private final Button deleteButton = new Button();
            AbstractEvent event;

            {
                // Ajouter une icône pour le bouton Modifier
                 ImageView editIcon = new ImageView(new Image(GestionEvenementsView .class.getResourceAsStream("/Images/edit.png")));
                if (editIcon.getImage() == null) {
                    throw new RuntimeException("Image resource not found: ../Images/edit.png");
                }
                
                editIcon.setFitWidth(16); // Largeur de l'icône
                editIcon.setFitHeight(16); // Hauteur de l'icône
                editButton.setGraphic(editIcon); // Définir l'icône sur le bouton
                editButton.setStyle("-fx-background-color: transparent;"); // Rendre le fond du bouton transparent

                // Ajouter une icône pour le bouton Supprimer
                ImageView deleteIcon = new ImageView(new Image(GestionEvenementsView .class.getResourceAsStream("/Images/delete.png")));
                if (deleteIcon.getImage() == null) {
                    throw new RuntimeException("Image resource not found: /Images/delete.png");
                }

                
                deleteIcon.setFitWidth(16);
                deleteIcon.setFitHeight(16);
                deleteButton.setGraphic(deleteIcon);
                deleteButton.setStyle("-fx-background-color: transparent;");

                // Ajouter les actions des boutons
                editButton.setOnAction(e -> {
                    event = getTableView().getItems().get(getIndex());
                    new ModifierEvenementView(stage, controller, event).show(event);
                });
                deleteButton.setOnAction(e -> {
                    event = getTableView().getItems().get(getIndex());
                    Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationDialog.setTitle("Confirmation de suppression");
                    confirmationDialog.setHeaderText("Voulez-vous vraiment supprimer cet événement ?");
                    confirmationDialog.setContentText("Titre : " + event.getTitle());
                    confirmationDialog.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            try {
                                controller.handleDeleteEvent(event);
                                eventData.remove(event);
                                refreshTable(null);
                            } catch (EventException ex) {
                                showAlert(Alert.AlertType.ERROR, "Erreur", ex.getMessage());
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
                }
            }
        });

        tableView.getColumns().addAll(titleColumn, locationColumn, dateColumn, descriptionColumn, categoryColumn, colorColumn, actionsColumn);

        // Colorier les lignes en fonction du code couleur des catégories
        /*
        tableView.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(AbstractEvent event, boolean empty) {
                super.updateItem(event, empty);
                if (empty || event == null) {
                    setStyle("");
                } else {
                    try {
                        String colorCode = controller.getCategoryColor(event.getIdCategory());
                        setStyle("-fx-background-color: " + colorCode + ";");
                    } catch (SQLException ex) {
                        setStyle("-fx-background-color: #FFFFFF;"); // Couleur par défaut
                    }
                }
            }
        }); */

        tableView.setItems(eventData);
        loadEventData(null);

        VBox mainContent = new VBox(20, title, head, tableView);
        mainContent.setPadding(new Insets(20));
        mainContent.setAlignment(Pos.TOP_CENTER);

        BorderPane layout = new BorderPane();
        layout.setLeft(menu);
        layout.setCenter(mainContent);

        Scene scene = new Scene(layout, 1000, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void loadEventData(String mot) {
        eventData.clear();
        eventData.addAll(controller.getAllEvents(mot));
    }

    private void refreshTable(String mot) {
        loadEventData(mot);
        tableView.refresh();
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
    Alert alert = new Alert(alertType); // Crée une nouvelle alerte du type spécifié
    alert.setTitle(title); // Définit le titre de l'alerte
    alert.setHeaderText(null); // Supprime le texte d'en-tête (peut être personnalisé si nécessaire)
    alert.setContentText(message); // Définit le contenu de l'alerte
    alert.showAndWait(); // Affiche l'alerte et attend que l'utilisateur interagisse avec elle
}

}
