
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import projetjavagestioneventement.MenuView;

public class GestionCategorieView {
    private final EventController controller;
    private final Stage stage;
    private TableView<EventCtegory> tableView;
    private ObservableList<EventCtegory> CatData;
    
    
    private boolean isMenuCollapsed   ;

    public GestionCategorieView(Stage stage, EventController controller) {
        this.stage = stage;
        this.controller = controller;
        this.CatData = FXCollections.observableArrayList();
        
        isMenuCollapsed = false;
    }

    public void show() { 
        
         // *** MENU VERTICAL COLLAPSIBLE ***
         
          VBox menu = new MenuView(stage,controller,isMenuCollapsed ).menu();
          
      
        
        Label title = new Label("Gestion des Catégories");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: #0078D7; -fx-font-weight: bold;");
        title.setAlignment(Pos.CENTER);

        tableView = new TableView<>();
        setupTableColumns();

        Button addCatButton = new Button("Ajouter Catégorie");
        addCatButton.setOnAction(e -> new AjouterCategoryView(stage, controller).show(null));
        
        
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20));
        mainContent.setAlignment(Pos.TOP_CENTER);
        
        mainContent.getChildren().addAll(title,tableView, addCatButton);
        BorderPane layout = new BorderPane();
        layout.setLeft(menu);
        layout.setCenter(mainContent);


        /*VBox layout = new VBox(20, title, tableView, addCatButton);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);*/

        Scene scene = new Scene(layout, 1000, 600);
        stage.setScene(scene);
        stage.show();

        loadCATData();
    }

    private void setupTableColumns() {
        TableColumn<EventCtegory, String> nomColumn = new TableColumn<>("Nom");
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));

        TableColumn<EventCtegory, String> couleurColumn = new TableColumn<>("Couleur");
        couleurColumn.setCellValueFactory(new PropertyValueFactory<>("colorCode"));

        TableColumn<EventCtegory, Void> modifyColumn = createModifyColumn();
        TableColumn<EventCtegory, Void> deleteColumn = createDeleteColumn();

        tableView.getColumns().addAll(nomColumn, couleurColumn, modifyColumn, deleteColumn);
        tableView.setItems(CatData);
    }

    private TableColumn<EventCtegory, Void> createModifyColumn() {
        TableColumn<EventCtegory, Void> modifyColumn = new TableColumn<>("Modifier");
        modifyColumn.setCellFactory(param -> new TableCell<>() {
            private final Button modifyButton = new Button("Modifier");

            {
                modifyButton.setOnAction(e -> {
                    EventCtegory category = getTableView().getItems().get(getIndex());
                    
                    new AjouterCategoryView(stage, controller).show(category);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : modifyButton);
            }
        });
        return modifyColumn;
    }

    private TableColumn<EventCtegory, Void> createDeleteColumn() {
        TableColumn<EventCtegory, Void> deleteColumn = new TableColumn<>("Supprimer");
        deleteColumn.setCellFactory(param -> {
            return new TableCell<>() {
                private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(e -> {
                    EventCtegory category = getTableView().getItems().get(getIndex());
                    
                    // Boîte de confirmation
                    Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationDialog.setTitle("Confirmation de suppression");
                    confirmationDialog.setHeaderText("Voulez-vous vraiment supprimer cette catégorie ?");
                    confirmationDialog.setContentText("Catégorie : " + category.getCategoryName());
                    
                    // Si l'utilisateur confirme, supprimer la catégorie
                    confirmationDialog.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            if (controller.handleDeleteCat(category)) {
                                refreshTable();
                                showInfoDialog("Catégorie supprimée avec succès !");
                            } else {
                                showErrorDialog("Impossible de supprimer la catégorie.");
                            }
                        }
                    });
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        };
    });
    return deleteColumn;
}



    private void loadCATData() {
        try {
            CatData.clear();
            CatData.addAll(controller.getAllCategories());
        } catch (SQLException ex) {
            showErrorDialog("Erreur lors du chargement des catégories.");
            Logger.getLogger(GestionCategorieView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void refreshTable() {
        loadCATData();
        tableView.refresh();
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }
    
    // Afficher un message d'information
    private void showInfoDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.showAndWait();
    }
    
  

}
