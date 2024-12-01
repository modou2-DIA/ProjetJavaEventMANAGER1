
package projetjavagestioneventement;

import Controller.Event.EventController;
import Modele.Event.EventStatistics;
import Vue.Event.GestionCategorieView;
import Vue.Event.GestionEvenementsView;
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
        stage.setTitle("EventEase - Application de Gestion");

        // *** MENU VERTICAL COLLAPSIBLE ***
        VBox menu = new VBox(15);
        menu.setPadding(new Insets(20));
        menu.setStyle("-fx-background-color: #2c3e50;");
        menu.setPrefWidth(200);

        // Bouton de bascule toujours visible
        toggleMenuButton = new Button("☰");
        toggleMenuButton.setStyle("-fx-background-color: #16a085; -fx-text-fill: white; -fx-font-size: 14px;");
        toggleMenuButton.setOnAction(e -> toggleMenu(menu)); // Appel à la méthode pour rétracter ou étendre

        Button homeButton = createMenuButton("Accueil");
        Button eventButton = createMenuButton("Gestion des événements");
        Button reservationButton = createMenuButton("Gestion des réservations");
        Button clientButton = createMenuButton("Gestion des clients");
        Button categoryButton = createMenuButton("Gestion des catégories");

        // Ajout des boutons au menu
        menu.getChildren().addAll(toggleMenuButton, homeButton, eventButton, reservationButton, clientButton, categoryButton);

        // Gestion des actions des boutons
        eventButton.setOnAction(e -> new GestionEvenementsView(stage, controller).show());
        homeButton.setOnAction(e -> new HomePageView1(stage, controller).show());
        categoryButton.setOnAction(e -> new GestionCategorieView(stage, controller).show());

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
        Label notificationLabel = new Label("Notifications :");
        notificationLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ListView<String> notificationList = new ListView<>();
        notificationList.setItems(FXCollections.observableArrayList(
                "3 événements commencent bientôt",
                "2 réservations en attente",
                "1 événement annulé"
        ));
        notificationList.setPrefHeight(50);
        EventStatistics stats = controller.getDashboardStatistics();
        VBox statisticsBox = createStatisticsBox(stats);
        mainContent.getChildren().addAll(dashboardTitle, welcomeTextBox,statisticsBox, chartsBox, notificationLabel, notificationList);

        // *** DISPOSITION GLOBALE ***
        BorderPane layout = new BorderPane();
        layout.setLeft(menu);
        layout.setCenter(mainContent);

        // *** SCENE ET STYLE ***
        Scene scene = new Scene(layout, 1000, 600);
        stage.setScene(scene);
        stage.show();
    }

    // Méthode pour créer un bouton de menu avec un style unifié
    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #16a085; -fx-text-fill: white;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;"));
        return button;
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

    // Méthode pour rendre le menu rétractable
    private void toggleMenu(VBox menu) {
        if (isMenuCollapsed) {
            menu.setPrefWidth(200);
            menu.getChildren().forEach(node -> node.setVisible(true));
            toggleMenuButton.setText("☰"); // Réinitialiser le texte du bouton
        } else {
            menu.setPrefWidth(50);
            menu.getChildren().forEach(node -> {
                if (node != toggleMenuButton) {
                    node.setVisible(false);
                }
            });
            toggleMenuButton.setText("☰"); // Maintenir le bouton visible
        }
        isMenuCollapsed = !isMenuCollapsed;
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
