package com.m8rten.gradle.prism.model

import spock.lang.Specification

class UserSpecification extends Specification {


    def 'Creates and update user from remote gradle invocations'(){
        given:
        Date date = new Date()
        RemoteGradleInvocation invocation = new RemoteGradleInvocation()
        invocation.commandLineTasks = ['yo', 'dude']
        User user = new User('Mr. Griffin');

        when: 'Mr. Griffin invokes gradle the first time running tasks yo and dude'
        user.invoked(invocation,date)

        then:
        user.userId == 'Mr. Griffin'
        user.lastInvocation == date
        user.nrOfInvocations == 1
        user.tasks.get(0).lastInvocation == date
        user.tasks.get(0).name == 'yo'
        user.tasks.get(0).nrOfInvocations == 1
        user.tasks.get(1).lastInvocation == date
        user.tasks.get(1).name == 'dude'
        user.tasks.get(1).nrOfInvocations == 1

        when: 'Mr. Griffin invokes gradle the second time ONLY running task yo'
        invocation.commandLineTasks = ['yo']
        user.invoked(invocation, date)

        then:
        user.userId == 'Mr. Griffin'
        user.lastInvocation == date
        user.nrOfInvocations == 2
        user.tasks.get(0).lastInvocation == date
        user.tasks.get(0).name == 'yo'
        user.tasks.get(0).nrOfInvocations == 2
        user.tasks.get(1).lastInvocation == date
        user.tasks.get(1).name == 'dude'
        user.tasks.get(1).nrOfInvocations == 1

    }
}
