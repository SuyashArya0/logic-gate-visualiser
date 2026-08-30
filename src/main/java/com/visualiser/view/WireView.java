package com.visualiser.view;

import com.visualiser.model.Wire;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;

public class WireView extends CubicCurve
{
    private final Wire wire;
    private final PinView sourcePinView;
    private final PinView targetPinView;

    public WireView(Wire wire, PinView sourcePinView, PinView targetPinView)
    {
        this.wire = wire;
        this.sourcePinView = sourcePinView;
        this.targetPinView = targetPinView;

        setStrokeWidth(3.0);
        setFill(null);

        bindCoordinates();
        updateVisualState();
    }

    private void bindCoordinates()
    {
        startXProperty().bind(sourcePinView.canvasXProperty());
        startYProperty().bind(sourcePinView.canvasYProperty());
        endXProperty().bind(targetPinView.canvasXProperty());
        endYProperty().bind(targetPinView.canvasYProperty());

        // Dynamic horizontal offset based on node distance
        DoubleBinding controlOffset = Bindings.createDoubleBinding(() -> {
            double deltaX = Math.abs(endXProperty().get() - startXProperty().get());
            return Math.max(deltaX * 0.5, 30.0);
        }, startXProperty(), endXProperty());

        // Control points create smooth horizontal Bezier curves
        controlX1Property().bind(startXProperty().add(controlOffset));
        controlY1Property().bind(startYProperty());
        controlX2Property().bind(endXProperty().subtract(controlOffset));
        controlY2Property().bind(endYProperty());
    }

    public void updateVisualState()
    {
        if(wire.getSourcePin().getState())
        {
            setStroke(Color.web("#2ECC71")); // HIGH signal
            setStrokeWidth(3.0);
        }
        else
        {
            setStroke(Color.web("#34495E")); // LOW signal
            setStrokeWidth(3.0);
        }
    }

    public Wire getWire() { return wire; }
}