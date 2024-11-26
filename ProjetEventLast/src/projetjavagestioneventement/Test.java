package projetjavagestioneventement;

import Connection.DatabaseConnection;
import Controller.Event.EventController;
import Modele.Event.EventDAO;
import Vue.Event.GestionEvenementsView;
import java.sql.Connection;
import java.sql.SQLException;

import javafx.application.Application;
 
import javafx.stage.Stage;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
public class Test extends Application {

    @Override
    public void start(Stage primaryStage) {
       
         try {
            Connection con = DatabaseConnection.getConnection();
            EventDAO eventDAO = new EventDAO(con);
            EventController controller = new EventController(eventDAO);
            
             GestionEvenementsView  views = new GestionEvenementsView (primaryStage,controller);
         views.show();
            
        } catch (SQLException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
               
 
    }

    public static void main(String[] args) {
        launch(args);
    }
}
*/

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Test extends Application {

    // List of Spectacles
    private final ObservableList<Spectacle> spectacleList = FXCollections.observableArrayList(
           
    );
    
    TableView<Spectacle> tableView = new TableView<>();
    
    // Charger les données depuis le contrôleur
    private void loadEventData() {
        spectacleList.clear();
        spectacleList.addAll(controller.getAllEvents());
    }

    // Rafraîchir la table
    private void refreshTable() {
        loadEventData();
        tableView.refresh();
    }

    @Override
    public void start(Stage primaryStage) {
        // Title of the window
        primaryStage.setTitle("Gestion des Spectacles");

        // Top section: Search bar and Add button
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher un spectacle...");
        Button searchButton = new Button("Rechercher");
        Button addButton = new Button("+ Ajouter un spectacle");

        HBox topBar = new HBox(10, searchField, searchButton, addButton);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #f4f4f4;");
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        
        VBox head = new VBox(10,topBar, resultArea);
        head.setPadding(new Insets(10));
       head.setStyle("-fx-background-color: #f4f4f4;");
       head.setAlignment(Pos.CENTER);
        head.setPadding(new Insets(10));
       
        searchButton.setOnAction(e -> {
           // var results = controller.handleSearchEvents(searchField.getText());
           // resultArea.setText(results.toString());
        });
        
        // TableView for Spectacles
        
        tableView.setItems(spectacleList);

        // Table columns
        TableColumn<Spectacle, String> titleColumn = new TableColumn<>("Titre");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Spectacle, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Spectacle, String> locationColumn = new TableColumn<>("Lieu");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        
         TableColumn<Spectacle, String> H_debutColumn = new TableColumn<>("Heure début");
        H_debutColumn.setCellValueFactory(new PropertyValueFactory<>("heureDebut"));
        
         TableColumn<Spectacle, String> dureeColumn = new TableColumn<>("DURÉE");
        dureeColumn.setCellValueFactory(new PropertyValueFactory<>("duree"));
        
         TableColumn<Spectacle, String> nbr_spectateursColumn = new TableColumn<>("Nombre spectateurs");
        nbr_spectateursColumn.setCellValueFactory(new PropertyValueFactory<>("spectateurs"));

        TableColumn<Spectacle, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {
                editButton.setOnAction(event -> {
                    Spectacle spectacle = getTableView().getItems().get(getIndex());
                    System.out.println("Modifier: " + spectacle.getTitle());
                });
                deleteButton.setOnAction(event -> {
                    Spectacle spectacle = getTableView().getItems().get(getIndex());
                    spectacleList.remove(spectacle);
                    System.out.println("Supprimé: " + spectacle.getTitle());
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

        // Add columns to the table
        tableView.getColumns().addAll(titleColumn, dateColumn, locationColumn,H_debutColumn ,dureeColumn, nbr_spectateursColumn,actionsColumn);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Main layout
        BorderPane root = new BorderPane();
        root.setTop(head);
        root.setCenter(tableView);

        // Add button action
        addButton.setOnAction(event -> {
            // Open a new window or dialog to add a spectacle
            System.out.println("Ajouter un spectacle");
        });
        
         addButton.setOnAction(e -> {
            String titre = titreField.getText();
            java.sql.Date date = java.sql.Date.valueOf(dateField.getValue());
            double heureDebut = Double.parseDouble(heureDebutField.getText());
            double duree = Double.parseDouble(dureeField.getText());
            int spectateurs = Integer.parseInt(spectateursField.getText());
            int lieu = Integer.parseInt(lieuField.getText());

            try (Connection conn = DatabaseConnection.getConnection1()) {
                CallableStatement stmt = conn.prepareCall("{call Pa_GestSpectacles.p_ajouter_spectacle(?, ?, ?, ?, ?, ?)}");
                stmt.setString(1, titre);
                stmt.setDate(2, date);
                stmt.setDouble(3, heureDebut);
                stmt.setDouble(4, duree);
                stmt.setInt(5, spectateurs);
                stmt.setInt(6, lieu);
                stmt.execute();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Spectacle ajouté avec succès !");
                alert.show();
            } catch (SQLException ex) {
                // Afficher le message d'erreur du trigger
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Échec de l'opération");
                alert.setContentText("Erreur lors de l'ajout du spectacle :\n" + ex.getMessage());
                alert.show();
            }catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erreur lors de l'ajout du spectacle.");
                alert.show();
            }
        });

        
        

        // Create scene and show stage
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Spectacle class (for TableView data)
    public static class Spectacle {
        private final String title;
        private final String date;
        private final String location;
        
        
        private final String heureDebut ;
       
        private final String duree ;
       
        private final String spectateurs;

        

        public Spectacle(String title, String date, String location, String heureDebut, String duree, String spectateurs) {
            this.title = title;
            this.date = date;
            this.location = location;
            this.heureDebut = heureDebut;
            this.duree = duree;
            this.spectateurs = spectateurs;
        }

        public String getTitle() {
            return title;
        }

        public String getDate() {
            return date;
        }

        public String getLocation() {
            return location;
        }

        public String getHeureDebut() {
            return heureDebut;
        }

        public String getDuree() {
            return duree;
        }

        public String getSpectateurs() {
            return spectateurs;
        }

        @Override
        public String toString() {
            return "Spectacle{" + "title=" + title + ", date=" + date + ", location=" + location + ", heureDebut=" + heureDebut + ", duree=" + duree + ", spectateurs=" + spectateurs + '}';
        }
        
        
        
        
        
        

        
        

        
    }
}

/*
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.CallableStatement;

public class Test extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gestion des Spectacles");

        // Layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Fields
        TextField titreField = new TextField();
        titreField.setPromptText("Titre");
        DatePicker dateField = new DatePicker();
        TextField heureDebutField = new TextField();
        heureDebutField.setPromptText("Heure de début (HH.MM)");
        TextField dureeField = new TextField();
        dureeField.setPromptText("Durée (heures)");
        TextField spectateursField = new TextField();
        spectateursField.setPromptText("Nombre de spectateurs");
        TextField lieuField = new TextField();
        lieuField.setPromptText("ID Lieu");

        Button submitButton = new Button("Ajouter Spectacle");

        // Add to grid
        grid.add(new Label("Titre:"), 0, 0);
        grid.add(titreField, 1, 0);
        grid.add(new Label("Date:"), 0, 1);
        grid.add(dateField, 1, 1);
        grid.add(new Label("Heure de début:"), 0, 2);
        grid.add(heureDebutField, 1, 2);
        grid.add(new Label("Durée:"), 0, 3);
        grid.add(dureeField, 1, 3);
        grid.add(new Label("Nombre de spectateurs:"), 0, 4);
        grid.add(spectateursField, 1, 4);
        grid.add(new Label("ID Lieu:"), 0, 5);
        grid.add(lieuField, 1, 5);
        grid.add(submitButton, 1, 6);

        // Button action
        submitButton.setOnAction(e -> {
            String titre = titreField.getText();
            java.sql.Date date = java.sql.Date.valueOf(dateField.getValue());
            double heureDebut = Double.parseDouble(heureDebutField.getText());
            double duree = Double.parseDouble(dureeField.getText());
            int spectateurs = Integer.parseInt(spectateursField.getText());
            int lieu = Integer.parseInt(lieuField.getText());

            try (Connection conn = DatabaseConnection.getConnection1()) {
                CallableStatement stmt = conn.prepareCall("{call Pa_GestSpectacles.p_ajouter_spectacle(?, ?, ?, ?, ?, ?)}");
                stmt.setString(1, titre);
                stmt.setDate(2, date);
                stmt.setDouble(3, heureDebut);
                stmt.setDouble(4, duree);
                stmt.setInt(5, spectateurs);
                stmt.setInt(6, lieu);
                stmt.execute();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Spectacle ajouté avec succès !");
                alert.show();
            } catch (SQLException ex) {
                // Afficher le message d'erreur du trigger
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Échec de l'opération");
                alert.setContentText("Erreur lors de l'ajout du spectacle :\n" + ex.getMessage());
                alert.show();
            }catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erreur lors de l'ajout du spectacle.");
                alert.show();
            }
        });

        // Scene
        Scene scene = new Scene(grid, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
*/