package com.m8rten.gradle.prism.plugin

import com.m8rten.gradle.prism.model.RemoteGradleInvocation
import groovyx.net.http.RESTClient
import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle

class Spy implements BuildListener {

    String url

    List<String> filter = []

    RESTClient restClient = new RESTClient()

    RemoteGradleInvocation gradleInvocation = new RemoteGradleInvocation()

    @Override
    void buildFinished(BuildResult result) {
        String userName = System.getProperty('user.name')
        if (!filter.contains(userName)) {
            gradleInvocation.userId = userName
            gradleInvocation.commandLineTasks = result.gradle.startParameter.taskNames.join(', ')
            restClient.uri = url
            restClient.post(body: gradleInvocation)
        }
    }

    @Override
    void buildStarted(Gradle gradle) {}

    @Override
    void projectsEvaluated(Gradle gradle) {}

    @Override
    void projectsLoaded(Gradle gradle) {}

    @Override
    void settingsEvaluated(Settings settings) {}
}