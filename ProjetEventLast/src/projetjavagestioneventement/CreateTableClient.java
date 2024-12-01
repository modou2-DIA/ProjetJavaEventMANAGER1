/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projetjavagestioneventement;

/**
 *
 * @author nadae
 */
import Connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTableClient {
    public static void createClientTable() {
        String createTableSQL = """
            CREATE TABLE Client (
                ClientID NUMBER   PRIMARY KEY,
                Nom VARCHAR2(100) NOT NULL,
                Prenom VARCHAR2(100) NOT NULL,
                Email VARCHAR2(150) UNIQUE,
                Telephone VARCHAR2(15),
                Adresse VARCHAR2(255),
                Ville VARCHAR2(100),
                CodePostal VARCHAR2(10),
                Pays VARCHAR2(100),
                DateNaissance DATE,
                DateCreation DATE DEFAULT SYSDATE
            )
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
            System.out.println("Table Client créée avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la création de la table Client.");
        }
    }

    public static void main(String[] args) {
        createClientTable();
    }
}

