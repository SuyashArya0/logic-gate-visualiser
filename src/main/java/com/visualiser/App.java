package com.visualiser;

import com.visualiser.model.GateType;
import com.visualiser.model.LogicNode;
import com.visualiser.view.CanvasPane;
import com.visualiser.factory.CircuitFactory;
import com.visualiser.util.StorageManager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

public class App extends Application
{
    private final StorageManager storageManager = new StorageManager();

    @Override
    public void start(Stage primaryStage)
    {
        CanvasPane canvasPane = new CanvasPane();

        // Top Toolbar Controls
        ToolBar topToolbar = new ToolBar();
        topToolbar.setStyle("-fx-background-color: #2C3E50; -fx-padding: 8px;");

        Button saveBtn = new Button("Save JSON");
        Button loadBtn = new Button("Load JSON");
        Button clearBtn = new Button("Clear Workspace");
        Button presetBtn = new Button("Load Half-Adder");

        topToolbar.getItems().addAll(saveBtn, loadBtn, new Separator(), clearBtn, new Separator(), presetBtn);

        // Sidebar Component Palette
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(10));
        sidebar.setStyle("-fx-background-color: #1A252F;");
        sidebar.setPrefWidth(140);
        sidebar.setAlignment(Pos.TOP_CENTER);

        Label paletteLabel = new Label("PALETTE");
        paletteLabel.setStyle("-fx-text-fill: #BDC3C7; -fx-font-weight: bold;");
        sidebar.getChildren().add(paletteLabel);

        for(GateType type : GateType.values())
        {
            Button addGateBtn = new Button("+ " + type.name());

            addGateBtn.setMaxWidth(Double.MAX_VALUE);
            addGateBtn.setStyle("-fx-background-color: #34495E; -fx-text-fill: white; -fx-cursor: hand;");
            addGateBtn.setOnAction(e -> canvasPane.addNode(new LogicNode(type, 200, 200)));

            sidebar.getChildren().add(addGateBtn);
        }

        // File Operations
        saveBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();

            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
            File file = chooser.showSaveDialog(primaryStage);

            if(file != null)
            {
                try
                {
                    storageManager.save(file, canvasPane.exportData());
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });

        loadBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();

            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
            File file = chooser.showOpenDialog(primaryStage);

            if(file != null)
            {
                try
                {
                    canvasPane.importData(storageManager.load(file));
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });

        clearBtn.setOnAction(e -> canvasPane.clear());
        presetBtn.setOnAction(e -> CircuitFactory.loadHalfAdder(canvasPane));

        // Main Layout
        BorderPane root = new BorderPane();
        root.setTop(topToolbar);
        root.setLeft(sidebar);
        root.setCenter(canvasPane);

        Scene scene = new Scene(root, 1100, 800, javafx.scene.paint.Color.web("#1E1E1E"));
        primaryStage.setTitle("Logic Gate Visualizer - Phase 3 Complete");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}