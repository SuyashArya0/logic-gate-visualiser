package com.visualiser;

import com.visualiser.engine.CircuitEvaluator;
import com.visualiser.model.GateType;
import com.visualiser.model.LogicNode;
import com.visualiser.model.Wire;
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
        LogicNode inputA = new LogicNode(GateType.INPUT, 100, 100);
        LogicNode inputB = new LogicNode(GateType.INPUT, 100, 200);
        LogicNode andGate = new LogicNode(GateType.AND, 300, 150);

        // Hardcode wire connections for phase 1 testing
        Wire wire1 = new Wire(inputA.getOutputPins().get(0), andGate.getInputPins().get(0));
        Wire wire2 = new Wire(inputB.getOutputPins().get(0), andGate.getInputPins().get(1));

        canvasPane.addNode(inputA);
        canvasPane.addNode(inputB);
        canvasPane.addNode(andGate);
        canvasPane.addWire(wire1);
        canvasPane.addWire(wire2);

        // Run an evaluation step
        CircuitEvaluator evaluator = new CircuitEvaluator();
        evaluator.evaluate(canvasPane.getNodes(), canvasPane.getWires());

        Scene scene = new Scene(canvasPane, 1024, 768, javafx.scene.paint.Color.web("#1E1E1E"));
        primaryStage.setTitle("Logic Gate Visualizer - Phase 1");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}