/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modele.Event;

/**
 *
 * @author DELL
 */

import java.sql.*;
 
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventDAO {
    private  List<AbstractEvent> events = new ArrayList<>();
     private Connection connection;

   
    // Constructeur principal
    public EventDAO(Connection connection) {
        this.connection = connection;
        // Charger les événements depuis la base de données lors de l'initialisation
        loadEventsFromDatabase();
    }

    // Charger les événements depuis la base de données
    private void loadEventsFromDatabase() {
        String sql = "SELECT * FROM events";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                // Vérifie si l'événement est récurrent
                int isRecurring = resultSet.getInt("is_recurring");
                AbstractEvent event;

                if (isRecurring==1) {
                    // Créer un événement récurrent
                    //(String recurrencePattern, String recurrence_period, LocalDateTime end_date, String description, int id, String title, LocalDateTime date, String location)
                    event = new RecurringEvent_1(
                            resultSet.getString("recurrence_pattern"),
                            resultSet.getString("recurrence_period"),
                            resultSet.getTimestamp("end_date").toLocalDateTime(),
                            resultSet.getString("description"),
                            resultSet.getInt("id"),
                            resultSet.getString("title"),
                            resultSet.getTimestamp("date_event").toLocalDateTime(),
                            resultSet.getString("location"),
                            isRecurring
                             
                    );
                } else {
                    // Créer un événement normal
                    event = new BasicEvent_1(
                            resultSet.getInt("id"),
                            resultSet.getString("title"),
                            resultSet.getTimestamp("date_event").toLocalDateTime(),
                            resultSet.getString("location"),
                            resultSet.getString("description"),
                            isRecurring
                    );
                }
                events.add(event);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des événements : " + e.getMessage());
        }
    }

    // Ajouter un événement
    public void addEvent(AbstractEvent event) throws SQLException {
        // Ajouter dans la base de données
        String sql = "INSERT INTO events (title, description, location, date_event, is_recurring, recurrence_period, end_date, recurrence_pattern) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, event.getTitle());
            statement.setString(2, event.getDescription());
            statement.setString(3, event.getLocation());
            statement.setString(4, event.getDates());

            if (event instanceof RecurringEvent_1) {
                RecurringEvent_1 recurringEvent = (RecurringEvent_1) event;
                statement.setInt(5, 1);
                statement.setString(6, recurringEvent.getRecurrence_period());
                statement.setTimestamp(7, Timestamp.valueOf(recurringEvent.getEnd_date()));
                statement.setString(8, recurringEvent.getRecurrencePattern());
            } else {
                statement.setInt(5, 0);
                statement.setNull(6, Types.VARCHAR);
                statement.setNull(7, Types.TIMESTAMP);
                statement.setNull(8, Types.VARCHAR);
            }

            statement.executeUpdate();

            
        }
    }

    // Modifier un événement
    public void updateEvent(AbstractEvent event) throws SQLException {
        String sql = "UPDATE events SET title = ?, description = ?, location = ?, date_event = ?, is_recurring = ?, recurrence_period = ?, end_date = ?, recurrence_pattern = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, event.getTitle());
            statement.setString(2, event.getDescription());
            statement.setString(3, event.getLocation());
            statement.setString(4, event.getDates());

            if (event instanceof RecurringEvent_1) {
                RecurringEvent_1 recurringEvent = (RecurringEvent_1) event;
                statement.setInt(5, 1);
                statement.setString(6, recurringEvent.getRecurrence_period());
                statement.setTimestamp(7, Timestamp.valueOf(recurringEvent.getEnd_date()));
                statement.setString(8, recurringEvent.getRecurrencePattern());
            } else {
                statement.setInt(5, 0);
                statement.setNull(6, Types.VARCHAR);
                statement.setNull(7, Types.TIMESTAMP);
                statement.setNull(8, Types.VARCHAR);
            }

            statement.setInt(9, event.getId());
            statement.executeUpdate();

            // Mettre à jour dans la liste locale
            events = events.stream()
                    .map(e -> e.getId() == event.getId() ? event : e)
                    .collect(Collectors.toList());
        }
    }

    // Supprimer un événement
    public void deleteEvent(int eventId) throws SQLException {
        String sql = "DELETE FROM events WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, eventId);
            statement.executeUpdate();

            // Supprimer de la liste locale
            events = events.stream()
                    .filter(e -> e.getId() != eventId)
                    .collect(Collectors.toList());
        }
    }

    // Rechercher des événements par mot-clé
    public List<AbstractEvent> searchEvents(String keyword) {
        return events.stream()
                .filter(e -> e.getTitle().toLowerCase().contains(keyword.toLowerCase())
                        || e.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Trier les événements par date
    // Afficher tous les événements
    public List<AbstractEvent> getAllEvents() {
        return events.stream()
                .sorted((e1, e2) -> e1.getDate().compareTo(e2.getDate()))
                .collect(Collectors.toList());
    }

    // Enregistrer les modifications locales dans la base de données
    public void saveAllEvents() throws SQLException {
        for (AbstractEvent event : events) {
            updateEvent(event); // Met à jour chaque événement dans la base
        }
    }
    // Ajouter une catégorie
    public void addCategory(EventCtegory category) throws SQLException {
        String sql = "INSERT INTO categories (category_name, color_code) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category.getCategoryName());
            statement.setString(2, category.getColorCode());
            statement.executeUpdate();
        }
    }

    // Récupérer toutes les catégories
    public List<EventCtegory> getAllCategories() throws SQLException {
        String sql = "SELECT * FROM categories";
        List<EventCtegory> categories = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                categories.add(new EventCtegory(
                        resultSet.getInt("id"),
                        resultSet.getString("category_name"),
                        resultSet.getString("color_code")
                ));
            }
        }
        return categories;
    }
    
     // Supprimer un événement
    public void deleteCategory(int catId) throws SQLException {
        String sql = "DELETE FROM categories  WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, catId);
            statement.executeUpdate();

           
           
        }
    }

    
}

