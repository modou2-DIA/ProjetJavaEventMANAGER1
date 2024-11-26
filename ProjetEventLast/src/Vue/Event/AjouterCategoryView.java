/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vue.Event;



/**
 *
 * @author DELL
 */

 
 

import Controller.Event.EventController;
 
import Modele.Event.EventCtegory;
import javafx.geometry.Insets;
 
import javafx.scene.Scene;
import javafx.scene.control.Button;
 

import javafx.scene.control.Label;

import javafx.scene.control.TextField;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class AjouterCategoryView{

    private final Stage stage;
     private EventController controller;

    public AjouterCategoryView(Stage stage,EventController controller) {
        this.stage = stage;
        this.controller = controller;
    }


    public void show(EventCtegory existingCategory) {
        // Fenêtre pour ajouter une catégorie
        Stage categoryStage = new Stage();
        categoryStage.setTitle("Ajouter une catégorie");

        Label categoryLabel = new Label("Nom de la catégorie :");
        TextField categoryNameField = new TextField();

        Label colorLabel = new Label("Code couleur (ex: #FF5733) :");
        TextField colorCodeField = new TextField();

        Button saveCategoryButton = new Button("Sauvegarder");
        saveCategoryButton.setOnAction(ev -> {
            try {
                EventCtegory category = new EventCtegory(0, categoryNameField.getText(), colorCodeField.getText());
                controller.handleAddCategory(category);
                categoryStage.close();
            } catch (Exception ex) {
                
            }
        });

        VBox categoryForm = new VBox(10, categoryLabel, categoryNameField, colorLabel, colorCodeField, saveCategoryButton);
        categoryForm.setPadding(new Insets(20));
        categoryForm.setStyle("-fx-border-color: gray; -fx-border-radius: 5;");

        categoryStage.setScene(new Scene(categoryForm, 400, 300));
        categoryStage.show();
    }
}


