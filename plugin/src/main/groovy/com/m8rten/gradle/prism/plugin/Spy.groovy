package com.m8rten.gradle.prism.plugin

import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle

class Spy implements BuildListener {

    String url

    List<String> filter

    @Override
    void buildFinished(BuildResult result){
        println "DONE"
        println url
        println filter
    }

    @Override void buildStarted(Gradle gradle){}

    @Override void projectsEvaluated(Gradle gradle){}

    @Override void projectsLoaded(Gradle gradle){}

    @Override void settingsEvaluated(Settings settings){}
}