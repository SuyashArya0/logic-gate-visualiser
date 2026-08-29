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
    }

    public void drawGrid(GraphicsContext gc)
    {
        gc.setStroke(Color.web("#2C3E50"));
        gc.setLineWidth(0.5);

        for(double x = 0; x < canvas.getWidth(); x += GRID_SIZE)
            gc.strokeLine(x, 0, x, canvas.getHeight());

        for(double y = 0; y < canvas.getHeight(); y += GRID_SIZE)
            gc.strokeLine(0, y, canvas.getWidth(), 0);
    }

    public void drawWires(GraphicsContext gc)
    {
        gc.setLineWidth(2);

        for(Wire wire : wires)
        {
            LogicNode sourceNode = findOwnerNode(wire.getSourcePin());
            LogicNode targetNode = findOwnerNode(wire.getTargetPin());

            if(sourceNode != null && targetNode != null)
            {
                // Highlight active signals green, inactive signals dark
                boolean active = wire.getSourcePin().getState();
                gc.setStroke(active ? Color.web("#2ECC71") : Color.web("#7F8C8D"));

                double startX = sourceNode.getX() + 80;
                double startY = sourceNode.getY() + 80;
                double endX = targetNode.getX();
                double endY = targetNode.getY() + 25;

                gc.strokeLine(startX, startY, endX, endY);
            }
        }
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
        return nodes.stream()
                .filter(n -> n.getInputPins().contains(pin) || n.getOutputPins().contains(pin))
                .findFirst()
                .orElse(null);
    }
}