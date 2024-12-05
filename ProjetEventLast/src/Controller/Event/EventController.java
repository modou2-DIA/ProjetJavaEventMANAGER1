/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller.Event;

import java.sql.*;
import Modele.Event.*;
import Modele.Notification.Notification;
import Modele.Notification.NotificationDAO;
import Modele.Reservations.Reservation;
import Modele.Reservations.ReservationDAO;
import Modele.client.clientDAO;
import java.time.format.DateTimeFormatter;

import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class EventController {
    private final EventDAO eventDAO;
    private final NotificationDAO notificationDAO;
    private final ReservationDAO reservationDAO ;
    private final clientDAO ClientDAO ;
            
   
 
    
    public EventController(EventDAO eventDAO, NotificationDAO notificationDAO,ReservationDAO reservationDAO,clientDAO ClientDAO) {
        this.eventDAO = eventDAO;
        this.notificationDAO = notificationDAO;
        this.reservationDAO = reservationDAO ;
        this.ClientDAO = ClientDAO;
    }
    
      public EventStatistics getDashboardStatistics() {
    try {
        return eventDAO.calculateEventStatistics();
    } catch (SQLException e) {
          System.err.println("Erreur SQL : " + e.getMessage());
        return new EventStatistics(0, 0); // Valeurs par défaut en cas d'erreur
    }
}


     public List<String> getAllEventTitles() {
    return eventDAO.getAllEventTitles(); // Récupère tous les titres des événements
}

     public List<AbstractEvent> handleSearchEvents(String keyword) {
        return eventDAO.searchEvents(keyword);
    }
    // Récupérer tous les événements
    public List<AbstractEvent> getAllEvents(String keyword) {
        return eventDAO.searchEvents(keyword);
    }
    // Supprimer un événement
    public void handleDeleteEvent(AbstractEvent event) throws EventException {
        
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
                try{
                eventDAO.addEvent(event);
            // Afficher un message de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Événement ajouté avec succès !");
            alert.showAndWait();
                }catch(SQLException ex)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Échec de l'opération");
                    alert.setContentText("Erreur lors de l'ajout d'un évenement \n la date de l evenement doit superieur á la date du jour:\n" );
                    alert.show();
                }
            }
            else
            {
                try{
                event.setId(evt.getId());
                eventDAO.updateEvent(event);
            // Afficher un message de succès
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Événement modifiés avec succès !");
                alert.showAndWait();
                }catch(SQLException ex)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Échec de l'opération");
                    alert.setContentText("""
                                         Erreur lors de la modification d'un evenement
                                         la date de l evenement doit superieur a la date du jour:
                                         """);
                    alert.show();
                }
                
            }
            
            
        } else {
            // Afficher une erreur si des champs obligatoires sont vides
            Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs obligatoires !");
            alert.showAndWait();
        }
    } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Échec de l'opération");
                alert.setContentText("Erreur innatendue lors de l 'operation :\n" + ex.getMessage());
                alert.show();
    }
        
    }
    
    public void checkEventConflict(AbstractEvent newEvent) throws EventException {
        eventDAO.checkEventConflict(newEvent);
    }
    
        public void addNotification(Notification notification) throws SQLException {
        notificationDAO.addNotification(notification);
    }

    public List<Notification> getAllNotifications() throws SQLException {
        return notificationDAO.getAllNotifications();
    } 
    
    public  List<String> getLastNotifications() throws SQLException{
        return notificationDAO.getLastNotifications();
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
    
    public String getCategoryColor(int id)  throws SQLException 
    {
        return eventDAO.getCategoryColor(id);
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
           Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Échec de l'opération");
                alert.setContentText("Erreur lors de la suppression :\n" + e.getMessage());
                alert.show();
          return false;
      }
  } 

    
    
    
    
    
    
    
    
    
     public List<Reservation> getAllReservations() throws SQLException{
         return reservationDAO.getAllReservations();
     }
     
     public void addReservation(Reservation reservation) throws SQLException 
     {
         reservationDAO.addReservation(reservation);
     }
     
     public void updateReservation(Reservation reservation) throws SQLException 
     {
         reservationDAO.updateReservation(reservation);
     }
     public void deleteReservation(int reservationId) throws SQLException
     {
         reservationDAO.deleteReservation(reservationId);
     }
     
     public List<Reservation> getReservationsByClientId(int clientId)
     {
         return reservationDAO.getReservationsByClientId(clientId);
     }
     
    public List<String> getAllClientNames() {
        return ClientDAO.getAllClientNames(); // Récupère tous les noms des clients
    }
    
    public String getClientNameById(int idclient)
    {
        return ClientDAO.getClientNameById(idclient);
    } 
    
    public boolean  addClient(String fullName, String email) throws Exception
    {
    try {
             ClientDAO.addClient(fullName, email);
             return true ;
         } catch (SQLException e) {
          System.err.println("Erreur SQL : " + e.getMessage());
          return false;
    }
        
    }
    
    public String getEventTitle(int id)
    {
        return eventDAO.getEventTitle(id);
    }

    public int getEventIdByTitle(String title) {
        return eventDAO.getEventIdByTitle(title);
    }

    public int getClientIdByName(String name) {
        return ClientDAO.getClientIdByName(name);
    }

  
    

}
