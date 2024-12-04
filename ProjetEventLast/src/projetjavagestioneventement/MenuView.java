/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projetjavagestioneventement;

import Controller.Event.EventController;
import Vue.Event.GestionCategorieView;
import Vue.Event.GestionEvenementsView;
import Vue.Notification.NotificationView;
import Vue.Reservation.GestionReservationsView;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author DELL
 */
public class MenuView {
    
    private final Stage stage;
    private final EventController controller;
    private Button toggleMenuButton;
     private boolean isMenuCollapsed;

    public MenuView(Stage stage, EventController controller, boolean isMenuCollapsed) {
        this.stage = stage;
        this.controller = controller;
        this.isMenuCollapsed = isMenuCollapsed;
    }

  
    
    public  VBox menu()
    {
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
            Button notificationButton = createMenuButton("Gestion des notifications");
            Button categoryButton = createMenuButton("Gestion des catégories");
            
            // Ajout des boutons au menu
            menu.getChildren().addAll(toggleMenuButton, homeButton, eventButton, reservationButton, notificationButton, categoryButton);
            
            // Gestion des actions des boutons
            eventButton.setOnAction(e -> new GestionEvenementsView(stage, controller).show());
            homeButton.setOnAction(e -> new HomePageView1(stage, controller).show());
            categoryButton.setOnAction(e -> new GestionCategorieView(stage, controller).show());
            notificationButton.setOnAction(e -> new NotificationView(stage,controller).show());
             reservationButton.setOnAction(e -> new GestionReservationsView(stage, controller).show());
             return menu ;
    }
    
    // Méthode pour créer un bouton de menu avec un style unifié
    public Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #16a085; -fx-text-fill: white;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;"));
        return button;
    }
    
    // Méthode pour rendre le menu rétractable
    public void toggleMenu(VBox menu) {
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
    
    
}
