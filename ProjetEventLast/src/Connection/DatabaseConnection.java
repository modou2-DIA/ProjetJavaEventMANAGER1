/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Connection;

/**
 *
 * @author DELL
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe"; // URL pour Oracle
    private static final String USER = "C###ProjetJavaACA";
    private static final String PASSWORD = "123456";
    
    private static final String USER1= "C###ADMIN";

    public static Connection getConnection() throws SQLException {
        Connection con =null; 
         try {
            // Charger le driver Oracle JDBC
            con =DriverManager.getConnection(URL, USER, PASSWORD);
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC Oracle introuvable", e);
        }
        return con;
    }
    
    public static Connection getConnection1() throws SQLException {
        Connection con =null; 
         try {
            // Charger le driver Oracle JDBC
            con =DriverManager.getConnection(URL, USER1, PASSWORD);
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC Oracle introuvable", e);
        }
        return con;
    }
}
