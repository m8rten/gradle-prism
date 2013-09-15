package com.m8rten.gradle.prism.model

import org.mongojack.Id

class User {
    @Id final String userId
    Date lastInvocation
    List<Task> tasks
    int nrOfInvocations

    User(String userId, Date date){
        this.userId = userId
        this.lastInvocation = date
        this.nrOfInvocations = 1
        this.tasks = []
    }
}
