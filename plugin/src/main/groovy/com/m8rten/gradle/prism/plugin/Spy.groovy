package com.m8rten.gradle.prism.plugin
import com.m8rten.gradle.prism.model.RemoteGradleInvocation
import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle

class Spy implements BuildListener {

    String url

    List<String> filter = []

    RESTClient restClient = new RESTClient()

    String userName = System.getProperty('user.name')

    RemoteGradleInvocation gradleInvocation = new RemoteGradleInvocation()

    Spy(Project project) {
        project.gradle.addBuildListener(this)
    }

    void filter(String... strings) {
        strings.each { filter.add(it) }
    }

    @Override
    void buildFinished(BuildResult buildResult) {
        if (userIsAllowedToSpyAt()) {
            loadGradleInvocationWith(buildResult)
            postGradleInvocation()
        }
    }

    private boolean userIsAllowedToSpyAt(){
        !filter.contains(userName)
    }

    private void loadGradleInvocationWith(BuildResult result){
        gradleInvocation.userId = userName
        gradleInvocation.commandLineTasks = result.gradle.startParameter.taskNames
    }

    private void postGradleInvocation(){
        restClient.uri = url
        restClient.contentType = ContentType.JSON
        try {
            restClient.post(body: gradleInvocation)
        } catch (Exception e) {
            // Shhh... be quiet...
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