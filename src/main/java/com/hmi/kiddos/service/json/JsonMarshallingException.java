package com.hmi.kiddos.service.json;

@SuppressWarnings("serial")
public class JsonMarshallingException extends RuntimeException
{
    public JsonMarshallingException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
