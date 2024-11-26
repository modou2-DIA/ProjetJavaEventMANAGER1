/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller.Event;


import Connection.DatabaseConnection;
import java.sql.*;
import Modele.Event.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import projetjavagestioneventement.Test;

public class EventController {
    private final EventDAO eventDAO;
    
    public EventController(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }
    
    public void handleAddCategory(EventCtegory category) {
    try {
        eventDAO.addCategory(category);
        System.out.println("Catégorie ajoutée : " + category.getCategoryName());
    } catch (SQLException e) {
    }
}
    public List<EventCtegory> getAllCategories() throws SQLException  {
        return eventDAO.getAllCategories();
    }
    
     // Supprimer un événement
    public void handleDeleteCat(EventCtegory event) {
        try {
            eventDAO.deleteEvent(event.getId()); // Supprimer dans la base
        } catch (SQLException e) {
            
        }
    }
    
     public List<AbstractEvent> handleSearchEvents(String keyword) {
        return eventDAO.searchEvents(keyword);
    }
    // Récupérer tous les événements
    public List<AbstractEvent> getAllEvents() {
        return eventDAO.getAllEvents();
    }
    // Supprimer un événement
    public void handleDeleteEvent(AbstractEvent event) {
        try {
            eventDAO.deleteEvent(event.getId()); // Supprimer dans la base
        } catch (SQLException e) {
            
        }
    }
  
     public void handleSubmit(TextField titleField, DatePicker datePicker, TextField locationField, TextArea descriptionArea) {
        try {
            // Récupérer les informations du formulaire
            String title = titleField.getText();
            String location = locationField.getText();
            String description = descriptionArea.getText();
            String dateTimeStr = null;

            // Convertir la date récupérée du DatePicker en String
            if (datePicker.getValue() != null) {
                // Formatter la date dans le format 'yyyy-MM-dd HH:mm:ss'
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                dateTimeStr = datePicker.getValue().atStartOfDay().format(formatter); // Convertir LocalDate à LocalDateTime et formater
            }

            // Vérifier que tous les champs sont remplis
            if (!title.isEmpty() && !location.isEmpty() && !description.isEmpty() && dateTimeStr != null) {
                // Connexion à la base de données
                Connection con = DatabaseConnection.getConnection();
                // Créer un objet BasicEvent avec les informations récupérées
                AbstractEvent be = new BasicEvent_1();
                be.setTitle(title);
                be.setDescription(description);
                be.setLocation(location);
                be.setDate(dateTimeStr);

                // Utiliser le DAO pour insérer l'événement dans la base de données             
                EventDAO bd = new EventDAO(con);
               bd.addEvent(be);
                // Afficher un message de succès
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Événement ajouté avec succès !");
                alert.showAndWait();
            } else {
                // Afficher une erreur si des champs sont vides
                Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs !");
                alert.showAndWait();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public  void handleCancel(Stage primaryStage) {
        // Fermer la fenêtre
        primaryStage.close();
    }

}
