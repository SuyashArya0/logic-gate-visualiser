package com.visualiser.view;

import com.visualiser.model.Pin;

import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PinView extends Circle
{
    private final Pin pin;
    private final GateNodeView parentNodeView;

    public PinView(Pin pin, GateNodeView parentNodeView)
    {
        super(6, Color.web("#7F8C8D"));

        this.pin = pin;
        this.parentNodeView = parentNodeView;

        setStroke(Color.web("#ECF0F1"));
        setStrokeWidth(1.5);

        updateVisualState();
    }

    public void updateVisualState()
    {
        if(pin.getState())
            setFill(Color.web("#2ECC71")); // HIGH - Green
        else
            setFill(Color.web("#7F8C8D")); // LOW - Grey
    }

    public Pin getPin() { return pin; }

    public GateNodeView getParentNodeView() { return parentNodeView; }

    public DoubleProperty canvasXProperty()
    {
        return parentNodeView.layoutXProperty().add(layoutXProperty());
    }

    public DoubleProperty canvasYProperty()
    {
        return parentNodeView.layoutYProperty().add(layoutYProperty());
    }
}