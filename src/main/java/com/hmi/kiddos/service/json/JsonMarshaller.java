package com.hmi.kiddos.service.json;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
public class JsonMarshaller
{
    private final ObjectMapper json = new ObjectMapper();


    public JsonMarshaller(Module... modules)
    {
        json.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        json.configure(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        json.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
        json.registerModule(new JodaModule());
        json.registerModules(modules);
    }

    public String toJson(Object o)
    {
        try
        {
            return json.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        }
        catch (IOException e)
        {
            throw new JsonMarshallingException("Failed to marshal object to json", e);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public String toCompactJson(Object o)
    {
        try
        {
            return this.json.writeValueAsString(o);
        }
        catch (IOException e)
        {
            throw new JsonMarshallingException("Failed to marshal object to json", e);
        }
    }

    public Map<String, Object> fromJson(String content)
    {
        //noinspection unchecked
        return fromJson(content, Map.class);
    }

    public <T> T fromJson(String content, Class<T> cls)
    {
        try
        {
            return json.readValue(content, cls);
        }
        catch (JsonParseException e)
        {
            throw new JsonParsingException("Failed to parse json", content, e);
        }
        catch (IOException e)
        {
            throw new JsonParsingException("IO error while parsing json", content, e);
        }
    }
}
