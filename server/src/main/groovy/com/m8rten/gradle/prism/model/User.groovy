package com.m8rten.gradle.prism.model

import groovy.transform.TupleConstructor
import org.mongojack.Id

@TupleConstructor class User {
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

    User(){}
}
