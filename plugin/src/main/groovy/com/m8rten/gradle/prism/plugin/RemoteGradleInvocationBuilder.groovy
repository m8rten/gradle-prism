package com.m8rten.gradle.prism.plugin
import com.m8rten.gradle.prism.model.SuperProject
import com.m8rten.gradle.prism.model.SuperRemoteGradleInvocation
import com.m8rten.gradle.prism.model.SuperTask
import org.gradle.api.Task

class RemoteGradleInvocationBuilder extends SuperRemoteGradleInvocation{

    void add(Task task){
        println task.state.failure == null
        SuperTask superTask = new SuperTask(name: task.name,
                success: task.state.failure == null,
                failedMessage: task.state.failure != null ? task.state.failure.message : null)
        if(hasProjectFor(task)){
            SuperProject superProject = new SuperProject(name: task.project.name)
            superProject.tasks.add(superTask)
            projects.add(superProject)
        } else {
            projectFor(task).tasks.add(superTask)
        }
    }

    private SuperProject projectFor(Task task) {
        projects.find {it.name.equals(task.project.name)}
    }

    private boolean hasProjectFor(Task task){
        projects.findAll{it.name.equals(task.project.name)}.size()==0
    }
}
