package com.hmi.kiddos.service;

import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

import com.hmi.kiddos.service.json.*;
import static org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace;

public abstract class RestServiceBase {

	private final JsonMarshaller jsonMarshaller = new JsonMarshaller();
	@Context
	protected UriInfo uriInfo;

	protected <T> Response resourceResponse(T value) {
		return value == null ? Response.status(Status.NOT_FOUND).build()
				: ok(value);
	}

	protected Response ok(Object value) {
		return Response.ok(asJson(value), MediaType.APPLICATION_JSON).build();
	}

	private String asJson(Object o) {
		return jsonMarshaller.toJson(o);
	}

	public static Response errorResponse(Status status, Throwable e) {
		return Response.status(status).entity(getFullStackTrace(e)).build();
	}

}
