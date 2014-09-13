package com.m8rten.gradle.prism.plugin

import com.m8rten.gradle.prism.model.User
import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle

class Spy implements BuildListener {

    String url

    List<String> filter = []

    RESTClient restClient = new RESTClient()

    String userName = System.getProperty('user.name')

    InvocationBuilder invocationBuilder = new InvocationBuilder()

    Spy(Project project) {
        project.gradle.addBuildListener(this)
        project.gradle.taskGraph.afterTask {Task task ->
            invocationBuilder.add(task)
        }
    }

    void filter(String... strings) {
        strings.each { filter.add(it) }
    }

    @Override
    void buildFinished(BuildResult buildResult) {
        invocationBuilder.user = new User(name: userName)
        restClient.uri = url
        restClient.contentType = ContentType.JSON
        try {
            restClient.post(body: invocationBuilder)
        } catch (Exception e) {
            // Shhh... be quiet...
        }

    }

    private boolean userIsAllowedToSpyAt(){
        !filter.contains(userName)
    }

    @Override
    void buildStarted(Gradle gradle) {
    }

    @Override
    void projectsEvaluated(Gradle gradle) {
    }

    @Override
    void projectsLoaded(Gradle gradle) {
    }

    @Override
    void settingsEvaluated(Settings settings) {
    }
}