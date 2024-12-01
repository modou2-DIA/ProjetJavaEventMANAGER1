package projetjavagestioneventement;

import Connection.DatabaseConnection;
import Controller.Event.EventController;
import Controller.Notification.NotificationController;
import Modele.Event.EventDAO;
import Vue.Notification.NotificationView;
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
            EventController controller = new EventController(eventDAO);
            HomePageView1 views = new HomePageView1 (primaryStage,controller);
            
            
            NotificationController notificationController = new NotificationController(DatabaseConnection.getConnection());
            NotificationView notificationView = new NotificationView(primaryStage, notificationController);
            notificationView.show();

            
    
         //views.show();
            
        } catch (SQLException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
               
 
    }

    public static void main(String[] args) {
        launch(args);
    }
}



