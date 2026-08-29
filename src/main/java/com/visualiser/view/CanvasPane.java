package com.visualiser.view;

import com.visualiser.model.LogicNode;
import com.visualiser.model.Wire;
import com.visualiser.model.Pin;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class CanvasPane extends Pane
{
    private final Canvas canvas;
    private final List<LogicNode> nodes = new ArrayList<>();
    private final List<Wire> wires = new ArrayList<>();

    public static final double GRID_SIZE = 20.0;

    public CanvasPane()
    {
        canvas = new Canvas();
        getChildren().add(canvas);

        // Auto resize canvas when parent window dimensions change
        widthProperty().addListener((obs, oldVal, newVal) -> {
            canvas.setWidth(newVal.doubleValue());
            redraw();
        });

        heightProperty().addListener((obs, oldVal, newVal) -> {
            canvas.setHeight(newVal.doubleValue());
            redraw();
        });
    }

    public void addNode(LogicNode node)
    {
        nodes.add(node);
        redraw();
    }

    public void addWire(Wire wire)
    {
        wires.add(wire);
        redraw();
    }

    public List<LogicNode> getNodes() { return nodes; }

    public List<Wire> getWires() { return wires; }

    public void redraw()
    {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        drawGrid(gc);
        drawNodes(gc);
        drawWires(gc);
    }

    public void drawGrid(GraphicsContext gc)
    {
        gc.setStroke(Color.web("#2C3E50"));
        gc.setLineWidth(0.5);

        for(double x = 0; x < canvas.getWidth(); x += GRID_SIZE)
            gc.strokeLine(x, 0, x, canvas.getHeight());

        for(double y = 0; y < canvas.getHeight(); y += GRID_SIZE)
            gc.strokeLine(0, y, canvas.getWidth(), y);
    }

    public void drawWires(GraphicsContext gc)
    {
        gc.setLineWidth(2.5);

        for(Wire wire : wires)
        {
            LogicNode sourceNode = findOwnerNode(wire.getSourcePin());
            LogicNode targetNode = findOwnerNode(wire.getTargetPin());

            if(sourceNode != null && targetNode != null)
            {
                // Highlight active signals green, inactive signals dark
                boolean active = wire.getSourcePin().getState();
                gc.setStroke(active ? Color.web("#2ECC71") : Color.web("#93726f"));

                // Calculate source pin Y index
                int outIndex = sourceNode.getOutputPins().indexOf(wire.getSourcePin());
                double startX = sourceNode.getX() + 80;
                double startY = sourceNode.getY() + getPinYOffset(sourceNode.getOutputPins().size(), outIndex);

                // Calculate target pin Y index
                int inIndex = targetNode.getInputPins().indexOf(wire.getTargetPin());
                double endX = targetNode.getX();
                double endY = targetNode.getY() + getPinYOffset(targetNode.getInputPins().size(), inIndex);

                gc.strokeLine(startX, startY, endX, endY);
            }
        }
    }

    private double getPinYOffset(int totalPins, int index)
    {
        if(totalPins <= 1)
            return 25.0; // Centered for a single pin

        double spacing = 50.0 / (totalPins + 1);
        return spacing * (index + 1);
    }

    public void drawNodes(GraphicsContext gc)
    {
        for(LogicNode node : nodes)
        {
            double x = node.getX();
            double y = node.getY();

            double width = 80;
            double height = 50;

            // Draw Node Box
            gc.setFill(Color.web("#34495E"));
            gc.setStroke(Color.web("#ECF0F1"));
            gc.setLineWidth(2);
            gc.fillRoundRect(x, y, width, height, 10, 10);
            gc.strokeRoundRect(x, y, width, height, 10, 10);
            
            // Draw Label
            gc.setFill(Color.WHITE);
            gc.fillText(node.getType().name(), x + 20, y + 30);
        }
    }

    private LogicNode findOwnerNode(Pin pin)
    {
        if (pin == null) 
            return null;
    
        return nodes.stream()
            .filter(n -> n.getInputPins().stream().anyMatch(p -> p.getId().equals(pin.getId())) ||
                         n.getOutputPins().stream().anyMatch(p -> p.getId().equals(pin.getId())))
            .findFirst()
            .orElse(null);
    }
}