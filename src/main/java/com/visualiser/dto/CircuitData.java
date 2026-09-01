package com.visualiser.dto;

import com.visualiser.model.GateType;

import java.util.ArrayList;
import java.util.List;

public class CircuitData
{
    public static class NodeDTO
    {
        public String id;
        public GateType type;
        public double x;
        public double y;

        public NodeDTO() {}

        public NodeDTO(String id, GateType type, double x, double y)
        {
            this.id = id;
            this.type = type;
            this.x = x;
            this.y = y;
        }
    }

    public static class WireDTO
    {
        public String sourceNodeId;
        public int sourcePinIndex;
        public String targetNodeId;
        public int targetPinIndex;

        public WireDTO() {}

        public WireDTO(String sourceNodeId, int sourcePinIndex, String targetNodeId, int targetPinIndex)
        {
            this.sourceNodeId = sourceNodeId;
            this.sourcePinIndex = sourcePinIndex;
            this.targetNodeId = targetNodeId;
            this.targetPinIndex = targetPinIndex;
        }
    }

    public List<NodeDTO> nodes = new ArrayList<>();
    public List<WireDTO> wires = new ArrayList<>();
}