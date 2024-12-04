/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modele.client;

/**
 *
 * @author DELL
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class clientDAO {

    private final Connection connection;

    public clientDAO(Connection connection) {
        this.connection = connection;
    }

    // Récupère tous les noms des clients
    public List<String> getAllClientNames() {
        List<String> names = new ArrayList<>();
        String query = "SELECT fullname FROM client"; // Suppose que la table s'appelle "clients" et contient une colonne "name"
        
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                names.add(resultSet.getString("fullname"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return names;
    }

    // Récupère l'ID d'un client par son nom
    public int getClientIdByName(String name) {
        String query = "SELECT id FROM client WHERE fullname = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return -1; // Retourne -1 si le client n'est pas trouvé
    }
    
public String getClientNameById(int idclient) { 
    String clientName = ""; // Modifier le nom de la variable pour plus de clarté
    String sql = """
                 select fullname from CLIENT c , RESERVATION  r 
                 where c.id = r.client_id  and r.client_id=?"""; // La colonne correcte est fullname
    
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
        // Associer l'ID du client à la requête
        statement.setInt(1, idclient);
        
        // Exécuter la requête
        ResultSet resultSet = statement.executeQuery();
        
        // Si un résultat est trouvé, récupérer le nom complet
        if (resultSet.next()) {
            clientName = resultSet.getString("fullname"); // Utiliser fullname
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors du chargement du client : " + e.getMessage());
    }

    return clientName;  // Retourner le nom complet du client
}

    public void addClient(String fullName, String email) throws Exception {
        String query = "INSERT INTO client (fullname, email) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, fullName);
            statement.setString(2, email);
            statement.executeUpdate();
        }
    }

}

