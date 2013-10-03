package com.m8rten.gradle.prism.model

import spock.lang.Specification

class TaskSpecification extends Specification{

    def 'Update tasks'(){
        given:
        Task task = new Task('name')
        Date date = new Date()

        expect:
        task.name == 'name'
        task.nrOfInvocations == 0
        task.lastInvocation == null

        when:
        task.wasRunAt(date)

        then:
        task.name == 'name'
        task.nrOfInvocations == 1
        task.lastInvocation == date
    }
}