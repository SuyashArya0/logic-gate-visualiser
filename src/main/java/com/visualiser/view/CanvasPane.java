package com.visualiser.view;

import com.visualiser.dto.CircuitData;
import com.visualiser.engine.CircuitEvaluator;
import com.visualiser.model.LogicNode;
import com.visualiser.model.Pin;
import com.visualiser.model.Wire;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class CanvasPane extends Pane 
{
    private final Canvas canvas;
    private final List<GateNodeView> nodeViews = new ArrayList<>();
    private final List<WireView> wireViews = new ArrayList<>();
    private final CircuitEvaluator evaluator = new CircuitEvaluator();

    private PinView pendingSourcePinView = null;
    private CubicCurve dragWirePreview = null;

    public static final double GRID_SIZE = 20.0;

    // REQUIRED for JavaFX FXML instantiation
    public CanvasPane() 
    {
        super();
        
        canvas = new Canvas();
        getChildren().add(canvas);

        // Auto-resize background grid canvas when parent window dimensions change
        widthProperty().addListener((obs, oldVal, newVal) -> {
            canvas.setWidth(newVal.doubleValue());
            drawGrid();
        });

        heightProperty().addListener((obs, oldVal, newVal) -> {
            canvas.setHeight(newVal.doubleValue());
            drawGrid();
        });

        setOnMouseMoved(this::handlePreviewWireDrag);
    }

    /*Adds a logic node model, creates its corresponding GateNodeView,
    and sets up user interactions for toggling and wiring.
    */
    public void addNode(LogicNode node) 
    {
        GateNodeView nodeView = new GateNodeView(node);
        nodeViews.add(nodeView);

        // Re evaluate circuit & refresh visualas across all gates/wires whenever an INPUT toggles
        nodeView.setOnToggleCallback(this :: evaluateCircuit);

        getChildren().add(nodeView);

        // Attach interactive wiring handlers to all node pins
        for (PinView pinView : nodeView.getOutputPinViews())
            pinView.setOnMouseClicked(e -> {
                e.consume();
                handlePinClick(pinView);
            });

        for (PinView pinView : nodeView.getInputPinViews())
            pinView.setOnMouseClicked(e -> {
                e.consume();
                handlePinClick(pinView);
            });
    }

    /*Programmatically adds a wire to the canvas by looking up the corresponding
    PinViews for the wire's source and target pins.
    */
    public void addWire(Wire wire) 
    {
        PinView sourcePinView = findPinView(wire.getSourcePin());
        PinView targetPinView = findPinView(wire.getTargetPin());

        if (sourcePinView != null && targetPinView != null) 
        {
            WireView wireView = new WireView(wire, sourcePinView, targetPinView);
            wireViews.add(wireView);
            getChildren().add(wireView);

            // Maintain proper layer ordering (wires behind nodes, above background)
            wireView.toBack();
            canvas.toBack();

            evaluateCircuit();
        }
    }

    // Retrieves the underlying LogicNode models present on the canvas.
    public List<LogicNode> getNodes() 
    {
        return nodeViews.stream()
                .map(GateNodeView::getNode)
                .toList();
    }

    // Retrieves a specific LogicNode model by its unique ID.s
    public Optional<LogicNode> getNode(String id) 
    {
        return nodeViews.stream()
                .map(GateNodeView::getNode)
                .filter(node -> node.getId().equals(id))
                .findFirst();
    }

    // Retrieves the underlying Wire models present on the canvas.
    public List<Wire> getWires() 
    {
        return wireViews.stream()
                .map(WireView::getWire)
                .toList();
    }

    public List<GateNodeView> getNodeViews() { return nodeViews; }
    public List<WireView> getWireViews() { return wireViews; }

    private void handlePinClick(PinView clickedPinView) 
    {
        if (pendingSourcePinView == null) 
        {
            // Start wire creation only when clicking an OUTPUT pin
            if (clickedPinView.getPin().getType() == Pin.Type.OUTPUT) 
            {
                pendingSourcePinView = clickedPinView;
                initWirePreview(clickedPinView);
            }
        } 
        else 
        {
            // Complete connection if clicking an INPUT pin on a different node
            if (clickedPinView.getPin().getType() == Pin.Type.INPUT &&
                clickedPinView.getParentNodeView() != pendingSourcePinView.getParentNodeView()) 
            {
                Wire wire = new Wire(pendingSourcePinView.getPin(), clickedPinView.getPin());
                addWire(wire);
            }

            cancelWirePreview();
        }
    }

    private void initWirePreview(PinView sourcePinView) 
    {
        dragWirePreview = new CubicCurve();
        dragWirePreview.setStroke(Color.web("#E67E22"));
        dragWirePreview.setStrokeWidth(2.5);
        dragWirePreview.getStrokeDashArray().addAll(6.0, 6.0);
        dragWirePreview.setFill(null);

        dragWirePreview.setMouseTransparent(true);

        dragWirePreview.setStartX(sourcePinView.canvasXProperty().get());
        dragWirePreview.setStartY(sourcePinView.canvasYProperty().get());
        dragWirePreview.setEndX(sourcePinView.canvasXProperty().get());
        dragWirePreview.setEndY(sourcePinView.canvasYProperty().get());

        getChildren().add(dragWirePreview);
    }

    private void handlePreviewWireDrag(MouseEvent event) 
    {
        if (dragWirePreview != null && pendingSourcePinView != null) 
        {
            dragWirePreview.setStartX(pendingSourcePinView.canvasXProperty().get());
            dragWirePreview.setStartY(pendingSourcePinView.canvasYProperty().get());
            dragWirePreview.setEndX(event.getX());
            dragWirePreview.setEndY(event.getY());

            dragWirePreview.setControlX1(dragWirePreview.getStartX() + 40);
            dragWirePreview.setControlY1(dragWirePreview.getStartY());
            dragWirePreview.setControlX2(event.getX() - 40);
            dragWirePreview.setControlY2(event.getY());
        }
    }

    private void cancelWirePreview() 
    {
        if (dragWirePreview != null) 
        {
            getChildren().remove(dragWirePreview);
            dragWirePreview = null;
        }

        pendingSourcePinView = null;
    }

    private PinView findPinView(Pin pin) 
    {
        if (pin == null) 
            return null;

        for (GateNodeView nodeView : nodeViews) 
        {
            for (PinView pinView : nodeView.getOutputPinViews())
                if (pinView.getPin().getId().equals(pin.getId())) 
                    return pinView;

            for (PinView pinView : nodeView.getInputPinViews())
                if (pinView.getPin().getId().equals(pin.getId())) 
                    return pinView;
        }

        return null;
    }

    public void evaluateCircuit() 
    {
        List<LogicNode> nodes = getNodes();
        List<Wire> wires = getWires();

        evaluator.evaluate(nodes, wires);

        nodeViews.forEach(GateNodeView::updateVisuals);
        wireViews.forEach(WireView::updateVisualState);
    }

    private void drawGrid() 
    {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        gc.setStroke(Color.web("#2C3E50"));
        gc.setLineWidth(0.5);

        for (double x = 0; x < canvas.getWidth(); x += GRID_SIZE)
            gc.strokeLine(x, 0, x, canvas.getHeight());

        for (double y = 0; y < canvas.getHeight(); y += GRID_SIZE)
            gc.strokeLine(0, y, canvas.getWidth(), y);
    }

    public void clear()
    {
        nodeViews.clear();
        wireViews.clear();
        getChildren().removeIf(node -> node != canvas);
        drawGrid();
    }

    private GateNodeView findNodeView(LogicNode node)
    {
        return nodeViews.stream()
            .filter(v -> v.getNode().getId().equals(node.getId()))
            .findFirst()
            .orElse(null);
    }

    public CircuitData exportData()
    {
        CircuitData data = new CircuitData();

        for(GateNodeView view : nodeViews)
        {
            LogicNode node = view.getNode();
            data.nodes.add(new CircuitData.NodeDTO(node.getId(), node.getType(), node.getX(), node.getY()));
        }

        for(WireView wireView : wireViews)
        {
            Wire wire = wireView.getWire();
            GateNodeView sourceView = nodeViews.stream()
                    .filter(v -> v.getNode().getOutputPins().contains(wire.getSourcePin()))
                    .findFirst()
                    .orElse(null);

            GateNodeView targetView = nodeViews.stream()
                    .filter(v -> v.getNode().getInputPins().contains(wire.getTargetPin()))
                    .findFirst()
                    .orElse(null);

            if(sourceView != null && targetView != null)
            {
                int sourceIdx = sourceView.getNode().getOutputPins().indexOf(wire.getSourcePin());
                int targetIdx = targetView.getNode().getInputPins().indexOf(wire.getTargetPin());

                data.wires.add(new CircuitData.WireDTO(sourceView.getNode().getId(), sourceIdx, targetView.getNode().getId(), targetIdx));
            }
        }

        return data;
    }

    public void importData(CircuitData data)
    {
        clear();
        Map<String, LogicNode> nodeMap = new HashMap<>();

        for(CircuitData.NodeDTO nodeDto : data.nodes)
        {
            LogicNode node = new LogicNode(nodeDto.type, nodeDto.x, nodeDto.y);
            nodeMap.put(nodeDto.id, node);
            addNode(node);
        }

        for(CircuitData.WireDTO wireDto : data.wires)
        {
            LogicNode sourceNode = nodeMap.get(wireDto.sourceNodeId);
            LogicNode targetNode = nodeMap.get(wireDto.targetNodeId);

            if(sourceNode != null && targetNode != null)
                connectPins(sourceNode, wireDto.sourcePinIndex, targetNode, wireDto.targetPinIndex);
        }

        evaluateCircuit();
    }

    public void connectPins(LogicNode sourceNode, int sourcePinIndex, LogicNode targetNode, int targetPinIndex)
    {
        GateNodeView sourceView = findNodeView(sourceNode);
        GateNodeView targetView = findNodeView(targetNode);

        if(sourceView != null && targetView != null)
        {
            PinView sourcePinView = sourceView.getOutputPinViews().get(sourcePinIndex);
            PinView targetPinView = targetView.getInputPinViews().get(targetPinIndex);

            Wire wire = new Wire(sourcePinView.getPin(), targetPinView.getPin());
            WireView wireView = new WireView(wire, sourcePinView, targetPinView);

            wireViews.add(wireView);
            getChildren().add(wireView);
            wireView.toBack();
            canvas.toBack();
        }
    }
}