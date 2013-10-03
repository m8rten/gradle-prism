package com.m8rten.gradle.prism.model

import org.mongojack.Id

class Task {
    @Id final String name
    int nrOfInvocations = 0
    Date lastInvocation

    Task(String name){
        this.name = name
    }

    void wasRunAt(Date date){
        nrOfInvocations++
        lastInvocation = date
    }

    Task(){}
}
