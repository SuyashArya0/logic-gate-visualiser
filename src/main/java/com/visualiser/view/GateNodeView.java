package com.visualiser.view;

import com.visualiser.model.LogicNode;
import com.visualiser.model.Pin;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class GateNodeView extends Pane
{
    private final LogicNode node;
    private final List<PinView> inputPinViews = new ArrayList<>();
    private final List<PinView> outputPinViews = new ArrayList<>();

    private double dragOffsetX;
    private double dragOffsetY;

    public GateNodeView(LogicNode node)
    {
        this.node = node;

        setLayoutX(node.getX());
        setLayoutY(node.getY());
        setPrefSize(100, 60);

        renderNodeUI();
        initDragAndDrop();
    }

    private void renderNodeUI()
    {
        Rectangle body = new Rectangle(100, 60);
        body.setArcWidth(12);
        body.setArcHeight(12);
        body.setFill(Color.web("#2C3E50"));
        body.setStroke(Color.web("#BDC3C7"));
        body.setStrokeWidth(2);

        Label label = new Label(node.getType().name());
        label.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px");

        VBox centerBox = new VBox(label);
        centerBox.setPrefSize(100, 60);
        centerBox.setAlignment(Pos.CENTER);

        getChildren().addAll(body, centerBox);

        // Position input pin UI circles
        List<Pin> inputs = node.getInputPins();

        for(int i = 0; i < inputs.size(); i++)
        {
            PinView pinView = new PinView(inputs.get(i), this);

            double spacing = 60.0 / (inputs.size() + 1);
            pinView.setLayoutX(0);
            pinView.setLayoutY(spacing * (i + 1));
            inputPinViews.add(pinView);
            getChildren().add(pinView);
        }

        // Position output pin UI circles
        List<Pin> outputs = node.getOutputPins();

        for(int i = 0; i < outputs.size(); i++)
        {
            PinView pinView = new PinView(outputs.get(i), this);

            double spacing = 60.0 / (outputs.size() + 1);
            pinView.setLayoutX(100);
            pinView.setLayoutY(spacing * (i + 1));
            outputPinViews.add(pinView);
            getChildren().add(pinView);
        }
    }

    private void initDragAndDrop()
    {
        setOnMousePressed(e -> {
            dragOffsetX = e.getSceneX() - getLayoutX();
            dragOffsetY = e.getSceneY() - getLayoutY();
            toFront(); // Bring selected node to foreground
        });

        setOnMouseDragged(e -> {
            double newX = Math.max(0, e.getSceneX() - dragOffsetX);
            double newY = Math.max(0, e.getSceneY() - dragOffsetY);

            // Snap to 20px grid
            newX = Math.round(newX / 20.0) * 20.0;
            newY = Math.round(newY / 20.0) * 20.0;

            setLayoutX(newX);
            setLayoutY(newY);
            node.setPosition(newX, newY);
        });
    }

    public LogicNode getNode() { return node; }

    public List<PinView> getInputPinViews() { return inputPinViews; }
    
    public List<PinView> getOutputPinViews() { return outputPinViews; }

    public void updateVisuals()
    {
        inputPinViews.forEach(PinView :: updateVisualState);
        outputPinViews.forEach(PinView :: updateVisualState);
    }
}