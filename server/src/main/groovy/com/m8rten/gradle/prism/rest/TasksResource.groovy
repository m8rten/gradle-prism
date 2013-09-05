package com.m8rten.gradle.prism.rest

import com.m8rten.gradle.prism.repository.TaskRepository
import com.fasterxml.jackson.databind.util.JSONPObject
import com.yammer.metrics.annotation.Timed

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

@Path("/task")
@Produces(MediaType.APPLICATION_JSON)
public class TasksResource {

    TaskRepository taskRepository

    @GET
    @Timed
    public JSONPObject getInvocations(@QueryParam("callback") String callback) {
        new JSONPObject(callback,taskRepository.getAll())
    }
}
