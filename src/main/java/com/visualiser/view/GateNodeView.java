package com.visualiser.view;

import com.visualiser.model.GateType;
import com.visualiser.model.LogicNode;
import com.visualiser.model.Pin;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;

public class GateNodeView extends Pane
{
    private final LogicNode node;
    private final Label titleLabel;
    private final List<PinView> inputPinViews = new ArrayList<>();
    private final List<PinView> outputPinViews = new ArrayList<>();

    private double dragOffsetX;
    private double dragOffsetY;

    private final Rectangle body;

    public GateNodeView(LogicNode node)
    {
        this.node = node;

        setLayoutX(node.getX());
        setLayoutY(node.getY());
        setPrefSize(100, 60);

        // Main Node Body Shape
        body = new Rectangle(100, 60);
        body.setArcWidth(12);
        body.setArcHeight(12);
        body.setFill(Color.web("#2C3E50"));
        body.setStroke(Color.web("#BDC3C7"));
        body.setStrokeWidth(2);

        // Label setup
        titleLabel = new Label();
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 13));
        titleLabel.setTextFill(Color.WHITE);

        VBox centerBox = new VBox(titleLabel);
        centerBox.setPrefSize(100, 60);
        centerBox.setAlignment(Pos.CENTER);

        getChildren().addAll(body, centerBox);

        renderPins();
        initDragAndDrop();
        updateVisuals();
    }

    private void renderPins()
    {
        List<Pin> inputs = node.getInputPins();
        for (int i = 0; i < inputs.size(); i++)
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
        for (int i = 0; i < outputs.size(); i++)
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
        boolean isActive = false;

        // Retrieve current state for INPUT or OUTPUT nodes
        if (node.getType() == GateType.INPUT) 
        {
            isActive = node.getOutputPins().get(0).getState();
            titleLabel.setText("INPUT: " + (isActive ? "HIGH (1)" : "LOW (0)"));
        } 
        else if (node.getType() == GateType.OUTPUT) 
        {
            isActive = node.getInputPins().get(0).getState();
            titleLabel.setText("OUTPUT: " + (isActive ? "HIGH (1)" : "LOW (0)"));
        } 
        else
            titleLabel.setText(node.getType().toString());

        // Apply visual feedback based on state
        if (isActive)
        {
            body.setFill(Color.web("#27AE60")); // Bright Green
            body.setStroke(Color.web("#2ECC71"));
        }
        else
        {
            body.setFill(Color.web("#2C3E50")); // Dark Slate
            body.setStroke(Color.web("#BDC3C7"));
        }

        // Update individual pin indicator colors
        inputPinViews.forEach(PinView :: updateVisualState);
        outputPinViews.forEach(PinView :: updateVisualState);
    }
}