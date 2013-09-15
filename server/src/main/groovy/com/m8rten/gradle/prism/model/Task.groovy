package com.m8rten.gradle.prism.model

import org.mongojack.Id

class Task {
    @Id final String name
    int nrOfInvocations
    Date lastInvocation

    Task(String name, Date lastInvocation){
        this.name = name
        this.lastInvocation = lastInvocation
        this.nrOfInvocations = 1
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false
        if (name != ((Task) o).name) return false
        return true
    }

    int hashCode() {
        return (name != null ? name.hashCode() : 0)
    }

    static List<Task> createTasks(String commandLineTasks, Date lastInvocation) {
        commandLineTasks.split(', ').collect { new Task(name: it, nrOfInvocations: 1, lastInvocation: lastInvocation) }
    }
}
