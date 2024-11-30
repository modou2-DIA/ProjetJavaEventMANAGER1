package Vue.Event;

import Controller.Event.EventController;
import Modele.Event.EventCtegory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AjouterCategoryView {

    private final Stage stage;
    private final EventController controller;

    public AjouterCategoryView(Stage stage, EventController controller) {
        this.stage = stage;
        this.controller = controller;
    }

    public void show(EventCtegory category) {
        // Titre de l'interface
        String titleText = (category == null) ? "Ajouter une catégorie" : "Modifier la catégorie";
        Label titleLabel = new Label(titleText);
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Champs de saisie
        Label nameLabel = new Label("Nom de la catégorie :");
        TextField nameField = new TextField();
        nameField.setPromptText("Entrez le nom de la catégorie");

        Label colorLabel = new Label("Code couleur (ex: #FF5733) :");
        TextField colorField = new TextField();
        colorField.setPromptText("Entrez le code couleur");

        // Pré-remplir les champs si une catégorie est fournie (modification)
        if (category != null) {
            nameField.setText(category.getCategoryName());
            colorField.setText(category.getColorCode());
        }

        // Bouton de sauvegarde
        Button saveButton = new Button((category == null) ? "Ajouter" : "Modifier");
        saveButton.setOnAction(e -> {
            String categoryName = nameField.getText();
            String colorCode = colorField.getText();

            // Validation des champs
            if (categoryName.isEmpty() || colorCode.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis !");
                return;
            }

            try {
                if (category == null) {
                    // Ajouter une nouvelle catégorie
                    EventCtegory newCategory = new EventCtegory(0, categoryName, colorCode);
                    controller.handleAddCategory(newCategory);
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Catégorie ajoutée avec succès !");
                } else {
                    // Modifier la catégorie existante
                    category.setCategoryName(categoryName);
                    category.setColorCode(colorCode);
                    controller.handleUpdateCategory(category);
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Catégorie modifiée avec succès !");
                }
                
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue : " + ex.getMessage());
            }
        });

        // Bouton d'annulation
        Button cancelButton = new Button("Annuler");
        cancelButton.setOnAction(e -> new GestionCategorieView( stage,  controller).show());

        // Mise en page
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(colorLabel, 0, 1);
        grid.add(colorField, 1, 1);

        VBox layout = new VBox(20, titleLabel, grid, saveButton, cancelButton);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        // Scène
        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    // Méthode utilitaire pour afficher des alertes
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
