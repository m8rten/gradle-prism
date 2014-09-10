package com.m8rten.gradle.prism.plugin
import com.m8rten.gradle.prism.model.Invocation
import com.m8rten.gradle.prism.model.Project
import org.gradle.api.Task

class InvocationBuilder extends Invocation{

    void add(Task task){
        println task.state.failure == null
        com.m8rten.gradle.prism.model.Task taskToSave = new com.m8rten.gradle.prism.model.Task(name: task.name,
                success: task.state.failure == null,
                failedMessage: task.state.failure != null ? task.state.failure.message : null)
        if(hasProjectFor(task)){
            Project project = new Project(name: task.project.name)
            project.tasks.add(taskToSave)
            projects.add(project)
        } else {
            projectFor(task).tasks.add(taskToSave)
        }
    }

    private Project projectFor(Task task) {
        projects.find {it.name.equals(task.project.name)}
    }

    private boolean hasProjectFor(Task task){
        projects.findAll{it.name.equals(task.project.name)}.size()==0
    }
}
