package com.m8rten.gradle.prism.rest

import com.m8rten.gradle.prism.model.RemoteGradleInvocation
import com.m8rten.gradle.prism.service.RemoteInvocationConverterService

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/invocation")
@Produces(MediaType.APPLICATION_JSON)
public class RemoteGradleInvocationResource {

    RemoteInvocationConverterService converter

    @POST
    @Consumes("application/json")
    public Response postInvocation(RemoteGradleInvocation remoteGradleInvocation) {
        converter.saveInformationRegarding(remoteGradleInvocation)
        Response.noContent().build()
    }
}
