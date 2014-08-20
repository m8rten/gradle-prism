package com.m8rten.gradle.prism.plugin

import com.m8rten.gradle.prism.model.SuperProject
import com.m8rten.gradle.prism.model.SuperRemoteGradleInvocation
import org.gradle.api.Task

class InvocationBuilder {

    List<Task> tasks

    SuperRemoteGradleInvocation build(){
        List<SuperProject> projects = tasks.collect{it.project.name}.unique().collect {new SuperProject(name: it)}
        tasks.each {

        }
    }
}
