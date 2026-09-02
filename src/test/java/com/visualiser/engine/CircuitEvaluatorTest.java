package com.visualiser.engine;

import com.visualiser.model.GateType;
import com.visualiser.model.LogicNode;
import com.visualiser.model.Wire;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CircuitEvaluatorTest
{
    private CircuitEvaluator evaluator;

    @BeforeEach
    void setUp()
    {
        evaluator = new CircuitEvaluator();
    }

    @Test
    @DisplayName("Test AND Logic Evaluation")
    void testAndGate()
    {
        LogicNode inA = new LogicNode(GateType.INPUT, 0, 0);
        LogicNode inB = new LogicNode(GateType.INPUT, 0, 100);
        LogicNode andGate = new LogicNode(GateType.AND, 200, 50);

        Wire w1 = new Wire(inA.getOutputPins().get(0), andGate.getInputPins().get(0));
        Wire w2 = new Wire(inB.getOutputPins().get(0), andGate.getInputPins().get(1));

        List<LogicNode> nodes = List.of(inA, inB, andGate);
        List<Wire> wires = List.of(w1, w2);

        // Input: 0, 0 -> Output: 0
        inA.getOutputPins().get(0).setState(false);
        inB.getOutputPins().get(0).setState(false);
        evaluator.evaluate(nodes, wires);
        assertFalse(andGate.getOutputPins().get(0).getState());

        // Input: 1, 0 -> Output: 0
        inA.getOutputPins().get(0).setState(true);
        inB.getOutputPins().get(0).setState(false);
        evaluator.evaluate(nodes, wires);
        assertFalse(andGate.getOutputPins().get(0).getState());

        // Input: 1, 1 -> Output: 1
        inA.getOutputPins().get(0).setState(true);
        inB.getOutputPins().get(0).setState(true);
        evaluator.evaluate(nodes, wires);
        assertTrue(andGate.getOutputPins().get(0).getState());
    }

    @Test
    @DisplayName("Test XOR Gate Logic Evaluation")
    void testXorGate()
    {
        LogicNode inA = new LogicNode(GateType.INPUT, 0, 0);
        LogicNode inB = new LogicNode(GateType.INPUT, 0, 100);
        LogicNode xorGate = new LogicNode(GateType.XOR, 200, 50);

        Wire w1 = new Wire(inA.getOutputPins().get(0), xorGate.getInputPins().get(0));
        Wire w2 = new Wire(inB.getOutputPins().get(0), xorGate.getInputPins().get(1));

        List<LogicNode> nodes = List.of(inA, inB, xorGate);
        List<Wire> wires = List.of(w1, w2);

        // Input: 1, 0 -> Output: 1
        inA.getOutputPins().get(0).setState(true);
        inB.getOutputPins().get(0).setState(false);
        evaluator.evaluate(nodes, wires);
        assertTrue(xorGate.getOutputPins().get(0).getState());

        // Input: 1, 1 -> Output: 0
        inA.getOutputPins().get(0).setState(true);
        inB.getOutputPins().get(0).setState(true);
        evaluator.evaluate(nodes, wires);
        assertFalse(xorGate.getOutputPins().get(0).getState());
    }

    @Test
    @DisplayName("Test NOT Gate Logic Evaluation")
    void testNotGate()
    {
        LogicNode inA = new LogicNode(GateType.INPUT, 0, 0);
        LogicNode notGate = new LogicNode(GateType.NOT, 200, 0);

        Wire w1 = new Wire(inA.getOutputPins().get(0), notGate.getInputPins().get(0));

        List<LogicNode> nodes = List.of(inA, notGate);
        List<Wire> wires = List.of(w1);

        // Input: 0 -> Output: 1
        inA.getOutputPins().get(0).setState(false);
        evaluator.evaluate(nodes, wires);
        assertTrue(notGate.getOutputPins().get(0).getState());

        // Input: 1 -> Output: 0
        inA.getOutputPins().get(0).setState(true);
        evaluator.evaluate(nodes, wires);
        assertFalse(notGate.getOutputPins().get(0).getState());
    }
}