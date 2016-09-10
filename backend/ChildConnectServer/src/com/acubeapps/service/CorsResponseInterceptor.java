package com.acubeapps.service;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class CorsResponseInterceptor implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext arg0, ContainerResponseContext arg1) throws IOException {
        arg1.getHeaders().add("Access-Control-Allow-Origin", "*");
        arg1.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT");
        arg1.getHeaders().add("Access-Control-Allow-Headers", "Content-Type, Accept");
    }
}