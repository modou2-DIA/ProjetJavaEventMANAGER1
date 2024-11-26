
/**
 *
 * @author DELL  GestionCategorieView
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vue.Event;
import Controller.Event.EventController;
import Modele.Event.EventCtegory;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GestionCategorieView {
    private final EventController controller;
    private final Stage stage;
    private TableView<EventCtegory> tableView; // Rend tableView accessible globalement
    private ObservableList<EventCtegory> CatData; // Données des événements

    public GestionCategorieView(Stage stage, EventController controller) {
        this.stage = stage;
        this.controller = controller;
        this.CatData = FXCollections.observableArrayList(); // Initialisation de la liste observable
    }

    public void show() {
        // Titre
        Label title = new Label("Gestion des Catégorie");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: #0078D7; -fx-font-weight: bold;");
        title.setAlignment(Pos.CENTER);
    
        // TableView des événements
        tableView = new TableView<>();
        TableColumn<EventCtegory, String> nomColumn = new TableColumn<>("Nom ");
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));

        TableColumn<EventCtegory , String> couleurColumn = new TableColumn<>("Couleur");
        couleurColumn.setCellValueFactory(new PropertyValueFactory<>("colorCode"));

         
        TableColumn<EventCtegory  , Void> modifyColumn = new TableColumn<>("Modifier");
        modifyColumn.setCellFactory(param -> new TableCell<>() {
            private final Button modifyButton = new Button("Modifier");

            {
                modifyButton.setOnAction(e -> {
                    EventCtegory  category = getTableView().getItems().get(getIndex());
                    new AjouterCategoryView(stage, controller).show(category);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(modifyButton);
                }
            }
        });

        TableColumn<EventCtegory  , Void> deleteColumn = new TableColumn<>("Supprimer");
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(e -> {
                    EventCtegory   event = getTableView().getItems().get(getIndex());
                    controller.handleDeleteCat(event); // Appel au contrôleur pour suppression
                    refreshTable(); // Rafraîchir la table
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
         
        tableView.getColumns().addAll(nomColumn, couleurColumn, modifyColumn, deleteColumn);
        // Lier les données de la table
        tableView.setItems(CatData);
        // Charger les données
        loadCATData();
        // Boutons principaux
        Button addCatButton = new Button("Ajouter Catégorie");
        addCatButton.setOnAction(e -> new AjouterCategoryView(stage, controller).show(null));

        VBox layout = new VBox(20, title,   tableView, addCatButton);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(layout, 1000, 800);
        stage.setScene(scene);
        stage.show();
    }

    // Charger les données depuis le contrôleur
    private void loadCATData()  {
        try {
            CatData.clear();
            CatData.addAll(controller.getAllCategories());
        } catch (SQLException ex) {
            Logger.getLogger(GestionCategorieView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Rafraîchir la table
    private void refreshTable()  {
        loadCATData();
        tableView.refresh();
    }
}

