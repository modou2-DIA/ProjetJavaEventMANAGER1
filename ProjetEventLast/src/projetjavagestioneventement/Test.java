package projetjavagestioneventement;

import Connection.DatabaseConnection;
import Controller.Event.EventController;
import Modele.Event.EventDAO;
import Modele.Notification.NotificationDAO;
import Modele.Reservations.ReservationDAO;
import Modele.client.clientDAO;
import java.sql.Connection;
import java.sql.SQLException;

import javafx.application.Application;
 
import javafx.stage.Stage;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Test extends Application {

    @Override
    public void start(Stage primaryStage) {
       
         try {
            Connection con = DatabaseConnection.getConnection();
            EventDAO eventDAO = new EventDAO(con);
            NotificationDAO notificationDAO= new NotificationDAO(con);
            ReservationDAO reservationDAO = new  ReservationDAO(con);
            clientDAO ClientDAO = new clientDAO(con);
            EventController  controller = new EventController(eventDAO,notificationDAO,reservationDAO,ClientDAO);
            HomePageView1 views = new HomePageView1 (primaryStage,controller);
            views.show();
            
        } catch (SQLException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
               
 
    }

    public static void main(String[] args) {
        launch(args);
    }
}



