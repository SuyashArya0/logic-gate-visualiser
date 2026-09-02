package com.visualiser.factory;

import com.visualiser.model.GateType;
import com.visualiser.model.LogicNode;
import com.visualiser.view.CanvasPane;

public class CircuitFactory
{
    public static void loadHalfAdder(CanvasPane canvas)
    {
        canvas.clear();

        LogicNode inA = new LogicNode(GateType.INPUT, 80, 120);
        LogicNode inB = new LogicNode(GateType.INPUT, 80, 280);

        LogicNode xorGate = new LogicNode(GateType.XOR, 280, 100);
        LogicNode andGate = new LogicNode(GateType.AND, 280, 280);

        LogicNode sumOut = new LogicNode(GateType.OUTPUT, 480, 100);
        LogicNode carryOut = new LogicNode(GateType.OUTPUT, 480, 280);

        canvas.addNode(inA);
        canvas.addNode(inB);
        canvas.addNode(xorGate);
        canvas.addNode(andGate);
        canvas.addNode(sumOut);
        canvas.addNode(carryOut);

        // Connect inputs to XOR (Sum) & AND (Carry)
        canvas.connectPins(inA, 0, xorGate, 0);
        canvas.connectPins(inB, 0, xorGate, 1);
        canvas.connectPins(inA, 0, andGate, 0);
        canvas.connectPins(inB, 0, andGate, 1);

        // Connect outputs
        canvas.connectPins(xorGate, 0, sumOut, 0);
        canvas.connectPins(andGate, 0, carryOut, 0);

        canvas.evaluateCircuit();
    }
}