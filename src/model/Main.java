package model;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    // Create a list of items
    private final ObservableList<String> items = FXCollections.observableArrayList();

    // Create the ComboBox
    private final ComboBox<String> comboBox = new ComboBox<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // Simple Interface
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));

        // Allow manual entry into ComboBox
        comboBox.setEditable(true);

        // Add sample items to our list
        items.addAll("One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten");

        createListener();

        root.getChildren().add(comboBox);

        // Show the stage
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Filtered ComboBox");
        primaryStage.show();
    }

    private void createListener() {

        // Create the listener to filter the list as user enters search terms
        FilteredList<String> filteredList = new FilteredList<>(items);

        // Add listener to our ComboBox textfield to filter the list
        comboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate(item -> {

                    // If the TextField is empty, return all items in the original list
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    // Check if the search term is contained anywhere in our list
                    if (item.toLowerCase().contains(newValue.toLowerCase().trim())) {
                        return true;
                    }

                    // No matches found
                    return false;
                }));

        // Finally, let's add the filtered list to our ComboBox
        comboBox.setItems(filteredList);

    }
}