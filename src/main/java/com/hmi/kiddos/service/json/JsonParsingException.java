package com.hmi.kiddos.service.json;

public class JsonParsingException extends RuntimeException
{
    @SuppressWarnings({"FieldCanBeLocal", "UnusedDeclaration"})
    private final String body;

    public JsonParsingException(String message, Throwable cause)
    {
        this(message, null, cause);
    }

    public JsonParsingException(String message, String body, Throwable cause)
    {
        super(message, cause);
        this.body = body;
    }
}
