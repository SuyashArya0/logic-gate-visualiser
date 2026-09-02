package com.visualiser.controller;

import com.visualiser.factory.CircuitFactory;
import com.visualiser.model.GateType;
import com.visualiser.model.LogicNode;
import com.visualiser.util.StorageManager;
import com.visualiser.view.CanvasPane;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class MainController
{
    @FXML private VBox paletteVBox;
    @FXML private CanvasPane canvas;

    private final StorageManager storageManager = new StorageManager();

    @FXML
    public void initialise()
    {
        // Dynamically populate palette buttons from GateType
        for(GateType type : GateType.values())
        {
            javafx.scene.control.Button btn = new javafx.scene.control.Button("+ " + type.name());
            btn.getStyleClass().add("palette-btn");
            btn.setOnAction(e -> canvas.addNode(new LogicNode(type, 200, 200)));
            paletteVBox.getChildren().add(btn);
        }
    }

    @FXML
    private void handleSave()
    {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));

        Stage stage = (Stage) canvas.getScene().getWindow();
        File file = chooser.showSaveDialog(stage);

        if (file != null) 
        {
            try 
            {
                storageManager.save(file, canvas.exportData());
            } 
            catch (Exception ex) 
            {
                showError("Save Error", "Could not save circuit file: " + ex.getMessage());
            }
        }
    }

    @FXML
    private void handleLoad()
    {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));

        Stage stage = (Stage) canvas.getScene().getWindow();
        File file = chooser.showOpenDialog(stage);

        if (file != null) 
        {
            try 
            {
                canvas.importData(storageManager.load(file));
            } 
            catch (Exception ex) 
            {
                showError("Load Error", "Could not load circuit file: " + ex.getMessage());
            }
        }
    }

    @FXML
    private void handleClear()
    {
        canvas.clear();
    }

    @FXML
    private void handleLoadHalfAdder()
    {
        CircuitFactory.loadHalfAdder(canvas);
    }

    private void showError(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}