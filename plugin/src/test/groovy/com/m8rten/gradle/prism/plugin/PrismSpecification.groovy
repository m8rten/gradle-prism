package com.m8rten.gradle.prism.plugin

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class PrismSpecification extends Specification {

    def 'adds spy extensions'(){
        given:
        Project project = ProjectBuilder.builder().build()

        when:
        project.apply(plugin: 'prism')

        then:
        project.extensions.findByName('spy')
    }

    def 'spy extensions'(){
        given:
        Project project = ProjectBuilder.builder().build()

        when:
        project.apply(plugin: 'prism')

        then:
        project.extensions.findByName('spy')

    }


}
