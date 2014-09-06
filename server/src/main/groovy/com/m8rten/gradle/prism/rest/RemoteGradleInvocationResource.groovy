package com.m8rten.gradle.prism.rest


import com.m8rten.gradle.prism.model.Invocation
import com.m8rten.gradle.prism.model.invocation.InvocationRepository

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/superinvocation")
@Produces(MediaType.APPLICATION_JSON)
public class RemoteGradleInvocationResource {

    InvocationRepository invocations

    @POST
    @Consumes("application/json")
    public Response postInvocation(Invocation invocation) {
        invocation.time = new Date()
        invocations.insert(invocation)
        Response.noContent().build()
    }
}
