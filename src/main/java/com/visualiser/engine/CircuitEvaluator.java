package com.visualiser.engine;

import com.visualiser.model.LogicNode;
import com.visualiser.model.Wire;

import java.util.List;

public class CircuitEvaluator
{
    /* Propogates values across wires and re evaluates all graph nodes
    iteratively until states stabilise or maximum propogation depth is reached */
    public void evaluate(List<LogicNode> nodes, List<Wire> wires)
    {
        int maxPasses = nodes.size() + 1; // Prevents infinite loop in cyclic circuits

        for(int i = 0; i < maxPasses; i++)
        {
            boolean changed = false;

            // Propogate state along wires
            for(Wire wire : wires)
            {
                boolean oldState = wire.getTargetPin().getState();

                wire.propagate();
                if(oldState != wire.getTargetPin().getState())
                    changed = true;
            }

            // Recalculate output pin states for all nodes
            for(LogicNode node : nodes)
                node.evaluate();

            if(!changed)
                break; // Circuit has stabilised
        }
    }
}