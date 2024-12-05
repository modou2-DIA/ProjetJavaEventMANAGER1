
package Modele.Event;


import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
 
import java.util.ArrayList;

import java.util.List;

import java.util.stream.Collectors;

public class EventDAO {
    private  List<AbstractEvent> events = new ArrayList<>();
     private final Connection connection;

   
    // Constructeur principal
    public EventDAO(Connection connection) {
        this.connection = connection;
        // Charger les événements depuis la base de données lors de l'initialisation
        loadEventsFromDatabase();
    }
    
    
    public EventStatistics calculateEventStatistics() throws SQLException {
    int totalEvents = 0;
    int monthlyEvents = 0;

    String totalQuery = "SELECT COUNT(*) AS total FROM events";
    String monthlyQuery = """
                          SELECT COUNT(*) AS monthly 
                          FROM events 
                          WHERE EXTRACT(MONTH FROM TO_DATE(date_event, 'YYYY-MM-DD HH24:MI:SS')) = EXTRACT(MONTH FROM CURRENT_DATE)""";

    try (Statement statement = connection.createStatement()) {
        // Total events
        ResultSet totalResult = statement.executeQuery(totalQuery);
        if (totalResult.next()) {
            totalEvents = totalResult.getInt("total");
        }

        // Monthly events
        ResultSet monthlyResult = statement.executeQuery(monthlyQuery);
        if (monthlyResult.next()) {
            monthlyEvents = monthlyResult.getInt("monthly");
        }
    }

    // Retourner un nouvel objet EventStatistics
    return new EventStatistics(totalEvents, monthlyEvents);
}


    // Charger les événements depuis la base de données
    private void loadEventsFromDatabase()  {
        String sql = "SELECT * FROM events";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                // Vérifie si l'événement est récurrent
                int isRecurring = resultSet.getInt("is_recurring");
                AbstractEvent event;
                String dateEventString = resultSet.getString("date_event");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String dateendString = resultSet.getString("end_date");
               
                
                if (isRecurring==1) {
                    // Créer un événement récurrent
                    //(String recurrencePattern, String recurrence_period, LocalDateTime end_date, String description, int id, String title, LocalDateTime date, String location)
                     LocalDateTime dateend = LocalDateTime.parse(dateendString, formatter);
                    event = new RecurringEvent_1(
                            resultSet.getString("recurrence_pattern"),
                            resultSet.getString("recurrence_period"),
                            dateend,
                            resultSet.getString("description"),
                            resultSet.getInt("id"),
                            resultSet.getString("title"),
                            resultSet.getTimestamp("date_event").toLocalDateTime(),
                            resultSet.getString("location"),
                            isRecurring
                            
                             
                    );
                    event.setIdCategory(resultSet.getInt("category_id"));
                } else {
                    // Créer un événement 
                    LocalDateTime dateEvent = LocalDateTime.parse(dateEventString, formatter);

                    event = new BasicEvent_1(
                            resultSet.getInt("id"),
                            resultSet.getString("title"),
                            dateEvent,
                            resultSet.getString("location"),
                            resultSet.getString("description"),
                            isRecurring
                    );
                     
                        event.setIdCategory(resultSet.getInt("category_id"));
                    
                    
                }
                events.add(event);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des événements : " + e.getMessage());
        }  
         
       
    }
    
   public String getEventTitle(int idevent) {
    String title = "";
    String sql = "SELECT title FROM events WHERE id = ?";
    
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
        // Associer l'ID de l'événement à la requête
        statement.setInt(1, idevent);
        
        // Exécuter la requête
        ResultSet resultSet = statement.executeQuery();
        
        // Si un résultat est trouvé, récupérer le titre
        if (resultSet.next()) {
            title = resultSet.getString("title");
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors du chargement de l'événement : " + e.getMessage());
    }

    return title;  // Retourner le titre de l'événement
}


    // Ajouter un événement
    public void addEvent(AbstractEvent event) throws SQLException {
        // Ajouter dans la base de données
        String sql = "INSERT INTO events (title, description, location, date_event, is_recurring, recurrence_period, end_date, recurrence_pattern,category_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, event.getTitle());
            statement.setString(2, event.getDescription());
            statement.setString(3, event.getLocation());
            statement.setString(4, event.getDates());

            if (event instanceof RecurringEvent_1 recurringEvent) {
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
            statement.setInt(9,event.getIdCategory() );
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

            if (event instanceof RecurringEvent_1 recurringEvent) {
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
    public void deleteEvent(int eventId) throws EventException {
        
        if (!eventExists(eventId)) {
        throw new EventException("L'événement avec l'ID " + eventId + " n'existe pas.");
    }
    try {
        String sql = "DELETE FROM events WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, eventId);
            statement.executeUpdate();
            
            
             // Supprimer de la liste locale
            events = events.stream()
                    .filter(e -> e.getId() != eventId)
                    .collect(Collectors.toList());
        }
    } catch (SQLException e) {
        throw new EventException("Erreur lors de la suppression de l'événement : " + e.getMessage());
    }
                  
    }

    
    
    private boolean eventExists(int eventId) {
        String sql = "SELECT 1 FROM events WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, eventId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            return false;
        }
    }
    // Rechercher des événements par mot-clé
    public List<AbstractEvent> searchEvents(String keyword) {
        if(keyword ==null)
        {
            return events.stream()
                .sorted((e1, e2) -> e1.getDate().compareTo(e2.getDate()))
                .collect(Collectors.toList());
        }
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

     
    
    // Récupère tous les titres des événements
    public List<String> getAllEventTitles() {
        List<String> titles = new ArrayList<>();
        String query = "SELECT title FROM events"; // Suppose que la table s'appelle "events" et contient une colonne "title"
        
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                titles.add(resultSet.getString("title"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return titles;
    }

    // Récupère l'ID d'un événement par son titre
    public int getEventIdByTitle(String title) {
        String query = "SELECT id FROM events WHERE title = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, title);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return -1; // Retourne -1 si l'événement n'est pas trouvé
    }
    
    //Si deux événements se chevauchent dans le temps , on lance une exception
    public void checkEventConflict(AbstractEvent newEvent) throws EventException {
    String sql = "SELECT * FROM events WHERE date_event = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setTimestamp(1, Timestamp.valueOf(newEvent.getDate()));
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            throw new EventException("Un autre événement est déjà prévu à cette date.");
        }
    } catch (SQLException e) {
        throw new EventException("Erreur lors de la vérification des conflits : " + e.getMessage());
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
    
     // Ajouter une catégorie
    public void updateCategory(EventCtegory category) throws SQLException {
        String sql = "UPDATE  categories set category_name=?, color_code=? where id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, category.getCategoryName());
            statement.setString(2, category.getColorCode());
            statement.setInt(3, category.getId());
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
    
      public   String getCategoryColor(int id)     {
        String query = "SELECT color_code FROM categories WHERE id= ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("color_code");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ""; // Retourne -1 si l'événement n'est pas trouvé
    }
    
    
    // Récupérer une catégorie par nom
public int getCategoryByName(String categoryName) throws SQLException {
    String sql = "SELECT id FROM categories WHERE category_name = ?";
    int id = -1; // Initialisation par une valeur par défaut

    // Utiliser un PreparedStatement pour éviter les injections SQL
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        preparedStatement.setString(1, categoryName); // Remplacer le paramètre

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                id = resultSet.getInt("id"); // Récupérer l'ID

                // Vérifier s'il y a plus d'une correspondance
                if (resultSet.next()) {
                    throw new SQLException("Plus d'une catégorie trouvée avec le même nom !");
                }
            } else {
                throw new SQLException("Aucune catégorie trouvée avec ce nom !");
            }
        }
    }
    return id;
}

public String getCategoryById(int id) throws SQLException {
    String sql = "SELECT category_name FROM categories WHERE id = ?";
    String  name = ""; // Initialisation par une valeur par défaut

    // Utiliser un PreparedStatement pour éviter les injections SQL
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        preparedStatement.setInt(1, id); // Remplacer le paramètre

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                name = resultSet.getString("category_name"); // Récupérer l'ID

                
            } else {
                throw new SQLException("Aucune catégorie trouvée avec cet identifiant !");
            }
        }
    }
    return name;
}

     // Supprimer un événement
   public boolean deleteCategory(int catId) throws SQLException {
    String sql = "DELETE FROM categories WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setInt(1, catId);
        int rowsAffected = statement.executeUpdate();
        return rowsAffected > 0; // Retourne true si une ligne a été supprimée
    }
}
   
}

