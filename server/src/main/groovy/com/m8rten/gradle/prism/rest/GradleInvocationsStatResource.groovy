package com.m8rten.gradle.prism.rest

import com.fasterxml.jackson.databind.util.JSONPObject
import com.yammer.metrics.annotation.Timed
import com.m8rten.gradle.prism.repository.GradleInvocationRepository

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/invocations/statistics/{nr}")
@Produces(MediaType.APPLICATION_JSON)
public class GradleInvocationsStatResource {

    GradleInvocationRepository invocationRepository

    @GET
    @Timed
    public JSONPObject getInvocations(@QueryParam("callback") String callback, @PathParam("nr") int nr) {
        new JSONPObject(callback,invocationRepository.getInvocationStatistics(nr))
    }
}
