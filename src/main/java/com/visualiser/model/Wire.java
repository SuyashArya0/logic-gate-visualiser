package com.visualiser.model;

import java.util.Objects;

public class Wire
{
    private final Pin sourcePin;
    private final Pin targetPin;

    public Wire(Pin sourcePin, Pin targetPin)
    {
        if(sourcePin.getType() != Pin.Type.OUTPUT || targetPin.getType() != Pin.Type.INPUT)
            throw new IllegalArgumentException("Wire must connect an OUTPUT pin to an INPUT pin.");

        this.sourcePin = sourcePin;
        this.targetPin = targetPin;
    }

    public void propagate()
    {
        targetPin.setState(sourcePin.getState());
    }

    public Pin getSourcePin() { return sourcePin; }
    public Pin getTargetPin() { return targetPin; }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;

        if(o == null || this.getClass() != o.getClass())
            return false;

        Wire wire = (Wire) o;
        return Objects.equals(this.sourcePin, wire.sourcePin) && Objects.equals(this.targetPin, wire.targetPin);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(sourcePin, targetPin);
    }
}