/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vue.Event;

import Connection.DatabaseConnection;
import Controller.Event.EventController;
import Modele.Event.AbstractEvent;
import Modele.Event.EventCtegory;
import Modele.Event.EventDAO;
import Modele.Notification.NotificationDAO;
import Modele.Reservations.ReservationDAO;
import Modele.client.clientDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author DELL
 */
public class ModifierEvenementView {
   private final Stage stage;
    private EventController controller;
    private AbstractEvent event;

   

    public ModifierEvenementView(Stage stage, EventController controller, AbstractEvent event) {
        this.stage = stage;
        this.controller = controller;
        this.event = event;
    }
    
    

    public ModifierEvenementView(Stage stage,EventController controller) {
        this.stage = stage;
        this.controller = controller;
    }

    public void show(AbstractEvent evt) {
        // Titre
        Label titleLabel = new Label("Modifier un événement");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Champs
        Label titleLabelfield = new Label("Titre de l'événement :");
        TextField titleField = new TextField();
        titleField.setPromptText("Titre de l'événement");
        titleField.setText(evt.getTitle());

        Label dateLabel = new Label("Date de l'événement :");
        DatePicker datePicker = new DatePicker();

        Label locationLabel = new Label("Lieu de l'événement :");
        TextField locationField = new TextField();
        locationField.setPromptText("Lieu de l'événement");
        locationField.setText(evt.getLocation());
        
         Label descriptionLabel = new Label("Decription de l'événement :");
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Description...");
        descriptionArea.setText(evt.getDescription());
        
        

       
        CheckBox isRecurringCheckBox = new CheckBox("Est récurrent ?");
        GridPane grid_recuring = new GridPane();
        grid_recuring.setVgap(10);
        grid_recuring.setHgap(10);
        grid_recuring.setPadding(new Insets(20));
        grid_recuring.setVisible(false);
        grid_recuring.setManaged(false);

        // Champs spécifiques pour événements récurrents
         
        Label recurrencePatternLabel = new Label(" Modèle de récurrence:");
        TextField recurrencePatternField = new TextField();
        recurrencePatternField.setPromptText("ex: hebdomadaire");
       
        
        
        
        Label endDatePickerLabel = new Label(" Date de fin  :");
        DatePicker endDatePicker = new DatePicker();
        
        grid_recuring.add(recurrencePatternLabel, 0, 0);
        grid_recuring.add(recurrencePatternField, 1, 0);
        grid_recuring.add(endDatePickerLabel, 0, 1);
        grid_recuring.add(endDatePicker, 1, 1);
        
       
        isRecurringCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
             grid_recuring.setVisible(newVal);
             grid_recuring.setManaged(newVal);
        });

        // Liste des catégories (RadioButton dynamique)
        Label categoryLabel = new Label("Catégorie de l'événement :");
        ToggleGroup categoryGroup = new ToggleGroup();
        VBox categoryBox = new VBox(10);
        categoryBox.setPadding(new Insets(10));
        categoryBox.getChildren().add(categoryLabel);

        try {
            // Récupération des catégories depuis le contrôleur
            for (EventCtegory categoryName : controller.getAllCategories()) {
                RadioButton categoryRadioButton = new RadioButton(categoryName.getCategoryName());
                categoryRadioButton.setToggleGroup(categoryGroup);
                categoryBox.getChildren().add(categoryRadioButton);
            }
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de la récupération des catégories !");
            alert.showAndWait();
        }

        Button saveButton = new Button("Modifier Évenement");
        
         saveButton.setOnAction(e -> {
            RadioButton selectedCategory = (RadioButton) categoryGroup.getSelectedToggle();
            if (selectedCategory != null) {
                String category = selectedCategory.getText(); 
                controller.handleUpdateOrAddEvent(titleField, datePicker, locationField, descriptionArea, isRecurringCheckBox,
                        recurrencePatternField,  endDatePicker, category,evt);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez sélectionner une catégorie !");
                alert.showAndWait();
            }
        });
         
  
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));

        grid.add(titleLabelfield, 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(dateLabel, 0, 1);
        grid.add(datePicker, 1, 1);
        grid.add(locationLabel, 0, 2);
        grid.add(locationField, 1, 2);
        grid.add(descriptionLabel, 0, 3);
        grid.add(descriptionArea, 1, 3);
        grid.add(grid_recuring,0,4);
         
        
        // Back button
        Button backButton = new Button("Retour");
        backButton.setOnAction(e -> {
            try {
                // Fermer l'écran actuel et ouvrir l'écran précédent
                Connection con = DatabaseConnection.getConnection();
                EventDAO eventDAO = new EventDAO(con);
                 NotificationDAO notificationDAO = new NotificationDAO (con);
                ReservationDAO reservationDAO = new  ReservationDAO(con);
                clientDAO ClientDAO = new clientDAO(con);
                controller = new EventController(eventDAO,notificationDAO,reservationDAO,ClientDAO);
                new GestionEvenementsView(stage,controller).show();
            } catch (SQLException ex) {
                Logger.getLogger(ModifierEvenementView.class.getName()).log(Level.SEVERE, null, ex);
            }
             
             
        });
         // Top HBox for back button and title
        HBox topLayout = new HBox(200, backButton, titleLabel);
        topLayout.setPadding(new Insets(10));
        topLayout.setAlignment(Pos.TOP_LEFT);

        // Scene setup
         
        VBox layout = new VBox(20, topLayout, grid,isRecurringCheckBox,  categoryBox, saveButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 1000, 600);
        stage.setScene(scene);
       stage.show();
        
        
         
       
         
    }
}
