package com.visualiser.model;

import java.util.List;

public enum GateType
{
    INPUT,
    OUTPUT,
    AND,
    OR,
    NOT,
    XOR;

    public boolean evaluate(List<Boolean> inputs)
    {
        return switch(this)
        {
            case INPUT -> !inputs.isEmpty() && inputs.get(0);
            case OUTPUT -> !inputs.isEmpty() && inputs.get(0);
            case AND -> inputs.size() >= 2 && inputs.stream().allMatch(b -> b);
            case OR -> inputs.stream().anyMatch(b -> b);
            case NOT -> inputs.isEmpty() || !inputs.get(0);
            case XOR -> inputs.size() >= 2 && (inputs.get(0) ^ inputs.get(1));
        };
    }
}