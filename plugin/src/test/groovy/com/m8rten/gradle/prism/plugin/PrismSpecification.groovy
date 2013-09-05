package com.m8rten.gradle.prism.plugin

import org.gradle.api.Project
import spock.lang.Specification

class PrismSpecification extends Specification {

    void apply(Project project) {
        project.extensions.create('spy', Spy)
        project.gradle.addBuildListener(new Spy())
    }

}
