package com.jersey.resources;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

	private static final Logger log = LogManager.getLogger(ImageResource.class);

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		log.info("Request filter");
		log.info("Headers: " + requestContext.getHeaders());
	}

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		log.info("Response filter");
		log.info("Headers: " + responseContext.getHeaders());
	}
}
