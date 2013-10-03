package com.m8rten.gradle.prism.model

import org.mongojack.Id

class GradleInvocation {
    @Id public String id;
    String userId
    List<String> commandLineTasks
    Date date

    GradleInvocation(RemoteGradleInvocation remoteGradleInvocation, Date date){
        userId = remoteGradleInvocation.userId
        commandLineTasks = remoteGradleInvocation.commandLineTasks
        this.date = date
    }

    GradleInvocation(){}
}
