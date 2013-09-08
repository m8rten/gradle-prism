package com.m8rten.gradle.prism.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class Prism implements Plugin<Project>{

    @Override
    void apply(Project project) {
        project.extensions.create('spy', Spy, project)
    }

}
