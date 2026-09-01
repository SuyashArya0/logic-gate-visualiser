package com.visualiser.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.visualiser.dto.CircuitData;

import java.io.File;
import java.io.IOException;

public class StorageManager
{
    private final ObjectMapper mapper;

    public StorageManager()
    {
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void save(File file, CircuitData data) throws IOException
    {
        mapper.writeValue(file, data);
    }

    public CircuitData load(File file) throws IOException
    {
        return mapper.readValue(file, CircuitData.class);
    }
}