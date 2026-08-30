package com.visualiser;

import com.visualiser.model.GateType;
import com.visualiser.model.LogicNode;
import com.visualiser.view.CanvasPane;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application
{
    @Override
    public void start(Stage primaryStage)
    {
        CanvasPane canvasPane = new CanvasPane();

        // Instantiate test nodes(AND gate fed by two input switches)
        LogicNode inputA = new LogicNode(GateType.INPUT, 80, 100);
        LogicNode inputB = new LogicNode(GateType.INPUT, 80, 240);
        LogicNode andGate = new LogicNode(GateType.AND, 280, 160);
        LogicNode outLED = new LogicNode(GateType.OUTPUT, 480, 160);

        canvasPane.addNode(inputA);
        canvasPane.addNode(inputB);
        canvasPane.addNode(andGate);
        canvasPane.addNode(outLED);

        Scene scene = new Scene(canvasPane, 1024, 768, javafx.scene.paint.Color.web("#1E1E1E"));
        primaryStage.setTitle("Logic Gate Visualizer - Phase 2(Interactive Canvas)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}