
package projetjavagestioneventement;

import Controller.Event.EventController;
import Modele.Event.EventStatistics;
import Vue.Event.GestionCategorieView;
import Vue.Event.GestionEvenementsView;
import Vue.Notification.NotificationView;
import Vue.Reservation.GestionReservationsView;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomePageView1 {

    private boolean isMenuCollapsed;
    private final Stage stage;
    private final EventController controller;
    private Button toggleMenuButton; // Bouton de bascule rendu accessible globalement

    public HomePageView1(Stage stage, EventController controller) {
        this.stage = stage;
        this.controller = controller;
        this.isMenuCollapsed = false; // Initialisation de l'état du menu
    }
    
    public void show() {
        try {
            stage.setTitle("EventEase - Application de Gestion");
            
            // Ajouter un conteneur pour les notifications en haut à droite
            
            // Récupérer les 3 dernières notifications depuis la base de données
            List<String> lastNotifications = controller.getLastNotifications();
            
            ListView<String> notificationList = new ListView<>();
            notificationList.setItems(FXCollections.observableArrayList(
                    lastNotifications
            ));
            notificationList.setPrefHeight(50);
            
            // *** MENU VERTICAL COLLAPSIBLE ***
            VBox menu = new MenuView(stage,controller,isMenuCollapsed ).menu();
           
            // *** ZONE PRINCIPALE ***
            VBox mainContent = new VBox(15);
            mainContent.setPadding(new Insets(15));
            mainContent.setAlignment(Pos.TOP_LEFT);
            
            // Titre principal
            Label dashboardTitle = new Label("Tableau de Bord");
            dashboardTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #34495e;");
            
            // Texte d'accueil
            VBox welcomeTextBox = new VBox(10);
            welcomeTextBox.setPadding(new Insets(10));
            welcomeTextBox.setAlignment(Pos.TOP_LEFT);
            
            Label welcomeTitle = new Label("Bienvenue dans EventEase !");
            welcomeTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #34495e;");
            
            Label welcomeText = new Label("""
                                                                  Simplifiez la gestion de vos événements avec EventEase :
                                                                  - Consultez vos données avec des graphiques interactifs.
                                                                  - Gérez facilement vos événements, catégories et réservations.
                                                                  - Restez informé grâce à des notifications en temps réel.
                                                  
                                                                  """);
            welcomeText.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");
            welcomeText.setWrapText(true);
            
            welcomeTextBox.getChildren().addAll(welcomeTitle, welcomeText);
            
            // Graphiques
            HBox chartsBox = new HBox(20);
            chartsBox.getChildren().addAll(createPieChart(), createBarChart());
            
            // Notifications
           
            EventStatistics stats = controller.getDashboardStatistics();
            VBox statisticsBox = createStatisticsBox(stats);
            mainContent.getChildren().addAll(dashboardTitle, welcomeTextBox,statisticsBox, chartsBox,notificationList);
            
            // *** DISPOSITION GLOBALE ***
            BorderPane layout = new BorderPane();
            layout.setLeft(menu);
            layout.setCenter(mainContent);
             
        

            // *** SCENE ET STYLE ***
            Scene scene = new Scene(layout, 1000, 600);
            stage.setScene(scene);
            stage.show();
        } catch (SQLException ex) {
            Logger.getLogger(HomePageView1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

    // Méthode pour créer un PieChart
    private PieChart createPieChart() {
        PieChart pieChart = new PieChart(FXCollections.observableArrayList(
                new PieChart.Data("Conférences", 40),
                new PieChart.Data("Ateliers", 30),
                new PieChart.Data("Webinaires", 20),
                new PieChart.Data("Autres", 10)
        ));
        pieChart.setTitle("Répartition des événements par catégorie");
        return pieChart;
    }

    // Méthode pour créer un BarChart
    private BarChart<String, Number> createBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Mois");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Nombre d'événements");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Événements par mois");

        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
        dataSeries.setName("2024");
        dataSeries.getData().add(new XYChart.Data<>("Janvier", 5));
        dataSeries.getData().add(new XYChart.Data<>("Février", 10));
        dataSeries.getData().add(new XYChart.Data<>("Mars", 15));
        dataSeries.getData().add(new XYChart.Data<>("Avril", 20));

        barChart.getData().add(dataSeries);
        return barChart;
    }

    
    private VBox createStatisticsBox(EventStatistics stats) {
    VBox statsBox = new VBox(10);
    statsBox.setPadding(new Insets(5));
    statsBox.setAlignment(Pos.CENTER_LEFT);

    Label totalEventsLabel = new Label("Nombre total d'événements : " + stats.totalEvents());
    totalEventsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

    Label monthlyEventsLabel = new Label("Événements ce mois-ci : " + stats.monthlyEvents());
    monthlyEventsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

    statsBox.getChildren().addAll(totalEventsLabel, monthlyEventsLabel);
    return statsBox;
}

}
