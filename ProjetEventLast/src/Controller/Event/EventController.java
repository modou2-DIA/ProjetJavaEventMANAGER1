/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller.Event;

import java.sql.*;
import Modele.Event.*;
import java.time.format.DateTimeFormatter;

import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import projetjavagestioneventement.Test;

public class EventController {
    private final EventDAO eventDAO;
    
    public EventController(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }
    
    public void handleAddCategory(EventCtegory category) {
    try {
        eventDAO.addCategory(category);
         
    } catch (SQLException e) {
    }  
            
}  
    public void handleUpdateCategory(EventCtegory category) {
    try {
        eventDAO.updateCategory(category);
         
    } catch (SQLException e) {
    }  
            
}
    
    public List<EventCtegory> getAllCategories() throws SQLException  {
        return eventDAO.getAllCategories();
    }
    
    public String getCategoryName(int id) throws SQLException 
    {
        return eventDAO.getCategoryById(id);
    }
    
    
     // Supprimer un événement
    public boolean handleDeleteCat(EventCtegory event) {
      try {
          return eventDAO.deleteCategory(event.getId());
      } catch (SQLException e) {
          System.err.println("Erreur lors de la suppression de la catégorie : " + e.getMessage());
          return false;
      }
  }

    
     public List<AbstractEvent> handleSearchEvents(String keyword) {
        return eventDAO.searchEvents(keyword);
    }
    // Récupérer tous les événements
    public List<AbstractEvent> getAllEvents(String keyword) {
        return eventDAO.searchEvents(keyword);
    }
    // Supprimer un événement
    public void handleDeleteEvent(AbstractEvent event) throws SQLException {
        
        eventDAO.deleteEvent(event.getId()); // Supprimer dans la base
        
    }
    
     //Ajouter ou modifier un evenement
    public void handleUpdateOrAddEvent(TextField titleField, DatePicker datePicker, TextField locationField, TextArea descriptionArea, 
                          CheckBox isRecurringCheckBox, TextField recurrencePatternField, DatePicker endDatePicker,String category,AbstractEvent evt)  {
        
        try {
        // Récupérer les informations du formulaire
        String title = titleField.getText();
        String location = locationField.getText();
        String description = descriptionArea.getText();
        String dateTimeStr = null;
         
        int idcategoru = eventDAO.getCategoryByName(category);
        

        // Convertir la date récupérée du DatePicker en String
        if (datePicker.getValue() != null) {
            // Formatter la date dans le format 'yyyy-MM-dd HH:mm:ss'
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            dateTimeStr = datePicker.getValue().atStartOfDay().format(formatter); // Convertir LocalDate à LocalDateTime et formater
        }

        // Vérifier que tous les champs obligatoires sont remplis
        if (!title.isEmpty() && !location.isEmpty() && !description.isEmpty() && dateTimeStr != null) {
            // Connexion à la base de données
            

            AbstractEvent event;
            
            // Vérifier si l'utilisateur a sélectionné un événement récurrent
            if (isRecurringCheckBox.isSelected()) {
                // Récupérer les informations supplémentaires pour les événements récurrents
                String recurrencePattern = recurrencePatternField.getText();
                String endDateStr = null;

                if (endDatePicker.getValue() != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    endDateStr = endDatePicker.getValue().atStartOfDay().format(formatter); // Convertir LocalDate à LocalDateTime et formater
                }

                // Vérifier que le modèle de récurrence et la date de fin sont fournis
                if (!recurrencePattern.isEmpty() && endDateStr != null) {
                    // Créer un objet RecurringEvent avec les informations récupérées
                    RecurringEvent_1 re = new RecurringEvent_1();
                    re.setIsRecurring(1);
                    re.setTitle(title);
                    re.setDescription(description);
                    re.setLocation(location);
                    re.setDate(dateTimeStr);
                    re.setRecurrencePattern(recurrencePattern);
                    re.setEnd_date(endDateStr);
                    re.setIdCategory(idcategoru);
                    

                    event = re;
                } else {
                    // Afficher une erreur si les champs de récurrence sont vides
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez remplir les informations de récurrence !");
                    alert.showAndWait();
                    return;
                }
            } else {
                // Créer un objet BasicEvent_1 avec les informations récupérées
                BasicEvent_1 be = new BasicEvent_1();
                be.setTitle(title);
                be.setDescription(description);
                be.setLocation(location);
                be.setDate(dateTimeStr);
                be.setIdCategory(idcategoru);
                event = be;
            }

             
            if(evt==null)
            {
                eventDAO.addEvent(event);
            // Afficher un message de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Événement ajouté avec succès !");
            alert.showAndWait();
            }
            else
            {
                event.setId(evt.getId());
                eventDAO.updateEvent(event);
            // Afficher un message de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Événement modifiés avec succès !");
            alert.showAndWait();
            }
            
            
        } else {
            // Afficher une erreur si des champs obligatoires sont vides
            Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs obligatoires !");
            alert.showAndWait();
        }
    } catch (SQLException ex) {
        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
    }
        
    }
    
  
    

}
//blabla