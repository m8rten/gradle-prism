package com.m8rten.gradle.prism.model

import org.mongojack.Id

class User {
    @Id final String userId
    Date lastInvocation
    List<Task> tasks = []
    int nrOfInvocations

    User(String userId){
        this.userId = userId;
    }

    void invoked(RemoteGradleInvocation remoteGradleInvocation, Date date){
        lastInvocation = date;
        nrOfInvocations++;
        tasks.findAll {remoteGradleInvocation.commandLineTasks.contains(it.name)}.each{
            it.lastInvocation = date
            it.nrOfInvocations++
        }
        remoteGradleInvocation.commandLineTasks.each { String taskName ->
            if (tasks.find { it.name == taskName } == null){
                Task task = new Task(taskName)
                task.wasRunAt(date)
                tasks.add(task)
            }
        }
    }

    User(){}
}
