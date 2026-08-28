package com.visualiser.model;

public class Pin
{
    public enum Type { INPUT, OUTPUT }

    private final String id;
    private final Type type;
    private boolean state;

    public Pin(String id, Type type)
    {
        this.id = id;
        this.type = type;
        this.state = false;
    }

    public String getId() { return id; }
    
    public Type getType() { return type; }

    public boolean getState() { return state; }
    public void setState(boolean state)
    {
        this.state = state;
    }
}