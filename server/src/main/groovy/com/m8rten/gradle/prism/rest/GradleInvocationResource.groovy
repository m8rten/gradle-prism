package com.m8rten.gradle.prism.rest

import com.m8rten.gradle.prism.model.GradleInvocation
import com.m8rten.gradle.prism.model.RemoteGradleInvocation
import com.m8rten.gradle.prism.model.Task
import com.m8rten.gradle.prism.repository.GradleInvocationRepository
import com.m8rten.gradle.prism.repository.TaskRepository
import com.m8rten.gradle.prism.repository.UserRepository

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/invocation")
@Produces(MediaType.APPLICATION_JSON)
public class GradleInvocationResource {

    GradleInvocationRepository invocationRepository

    UserRepository userRepository

    TaskRepository taskRepository

    @POST
    @Consumes("application/json")
    public Response postInvocation(RemoteGradleInvocation remoteGradleInvocation) {

        GradleInvocation invocation = new GradleInvocation()
        invocation.commandLineTasks = remoteGradleInvocation.commandLineTasks
        invocation.userId = remoteGradleInvocation.userId

        Date currentDate = new Date()
        invocation.date = currentDate
        invocationRepository.insert(invocation)

        List<Task> invokedTasks = Task.createTasks(invocation.commandLineTasks, currentDate)

        if (userRepository.contains(invocation.userId))
            userRepository.update(invocation.userId,invokedTasks,currentDate)
        else userRepository.insert(invocation.userId,invokedTasks,currentDate)

        invokedTasks.each {
            if (taskRepository.contains(it))
                taskRepository.update(it)
            else taskRepository.insert(it)
        }

        Response.noContent().build()
    }
}
