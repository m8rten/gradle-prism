package com.m8rten.gradle.prism.plugin
import com.m8rten.gradle.prism.model.Invocation
import com.m8rten.gradle.prism.model.Project
import com.m8rten.gradle.prism.model.Task

class InvocationBuilder extends Invocation{

    void add(org.gradle.api.Task task){
       Task taskToSave = new Task(name: task.name,
                success: task.state.failure == null,
                failedMessage: task.state.failure != null ? task.state.failure.message : null)
        if(projectHasBeenSavedFor(task)){
            Project project = new Project(name: pathFor(task))
            project.tasks.add(taskToSave)
            projects.add(project)
        } else {
            projectFor(task).tasks.add(taskToSave)
        }
    }

    private Project projectFor(org.gradle.api.Task task) {
        projects.find {it.name.equals(pathFor(task))}
    }

    private boolean projectHasBeenSavedFor(org.gradle.api.Task task){
        projects.findAll{it.name.equals(pathFor(task))}.size()==0
    }

    private String pathFor(org.gradle.api.Task task){
        task.project.rootProject.name+task.project.path
    }
}
