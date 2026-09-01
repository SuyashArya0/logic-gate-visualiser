package com.visualiser.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LogicNode
{
    private final String id;
    private final GateType type;
    private final List<Pin> inputPins;
    private final List<Pin> outputPins;

    private double x;
    private double y;

    public LogicNode(GateType type, double x, double y)
    {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.x = x;
        this.y = y;
        this.inputPins = new ArrayList<>();
        this.outputPins = new ArrayList<>();

        initPins();
    }

    private void initPins()
    {
        int inCount = (type == GateType.NOT || 
            type == GateType.INPUT || 
            type == GateType.OUTPUT) ? 
            1 : 2;

        for(int i = 0; i < inCount; i++)
            inputPins.add(new Pin(id + "-in-" + i, Pin.Type.INPUT));

        if(type != GateType.OUTPUT)
            outputPins.add(new Pin(id + "-out-0", Pin.Type.OUTPUT));
    }

    public void evaluate()
    {
        // Skip re evaluation for INPUT nodes so manual toggles are preserved
        if(type == GateType.INPUT)
            return;

        List<Boolean> inputStates = inputPins.stream().map(Pin :: getState).toList();
        boolean result = type.evaluate(inputStates);

        outputPins.forEach((pin -> pin.setState(result)));
    }

    public String getId() { return id; }

    public GateType getType() { return type; }
    
    public List<Pin> getInputPins() { return inputPins; }
    public List<Pin> getOutputPins() { return outputPins; }

    public double getX() { return x; }
    public double getY() { return y; }

    public void setPosition(double x, double y)
    {
        this.x = x;
        this.y = y;
    }
}