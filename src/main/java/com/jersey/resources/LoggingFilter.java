package com.jersey.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
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

	private static final Logger log = LogManager.getLogger(LoggingFilter.class);

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		log.info("=============Request Filter=============");
		log.info("Request Acceptable MediaType: " +  requestContext.getAcceptableMediaTypes());
		log.info("Request MediaType: " + requestContext.getMediaType());
		log.info("Headers: " + requestContext.getHeaders());
		log.info("Request Absolute Path: " + requestContext.getUriInfo().getAbsolutePath());
        log.info("Request Security Context: " + requestContext.getSecurityContext().getAuthenticationScheme());
	}

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		log.info("============= Response Filter=============");
        log.info("Response Allowed Methods: " +  responseContext.getAllowedMethods());
        log.info("Response MediaType: " + responseContext.getMediaType());
        log.info("Headers: " + responseContext.getHeaders());

        Object entity = responseContext.getEntity();
        if (entity != null) {
            log.info("Response " + new ObjectMapper().writeValueAsString(entity));
        }
	}
}
