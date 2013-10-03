package com.m8rten.gradle.prism.service

import com.m8rten.gradle.prism.model.GradleInvocation
import com.m8rten.gradle.prism.model.RemoteGradleInvocation
import com.m8rten.gradle.prism.model.Task
import com.m8rten.gradle.prism.model.User
import com.m8rten.gradle.prism.repository.GradleInvocationRepository
import com.m8rten.gradle.prism.repository.StatisticsRepository
import com.m8rten.gradle.prism.repository.TaskRepository
import com.m8rten.gradle.prism.repository.UserRepository
import spock.lang.Specification

class RemoteInvocationConverterServiceSpecification extends Specification {

    RemoteInvocationConverterService converterService
    RemoteGradleInvocation remoteGradleInvocation
    UserRepository userRepository
    TaskRepository taskRepository
    GradleInvocationRepository gradleInvocationRepository
    StatisticsRepository statisticsRepository

    def 'Creates gradle invocation and saves to repository'() {
        given:
        'Remote invocation, repositories and service'()

        when:
        converterService.saveInformationRegarding(remoteGradleInvocation)

        then:
        1 * gradleInvocationRepository.insert({GradleInvocation invocation ->
            invocation.userId == remoteGradleInvocation.userId
            invocation.commandLineTasks == remoteGradleInvocation.commandLineTasks
            invocation.date == converterService.date
        })
    }

    def 'Updates (changing date value and nr of invocations) and create tasks in repository'(){
        given:
        'Remote invocation, repositories and service'()
        taskRepository.containsName('task1') >> true
        Task task1 = new Task('task1')
        task1.nrOfInvocations = 1
        taskRepository.withName('task1') >> task1
        taskRepository.containsName('task2') >> false

        when:
        converterService.saveInformationRegarding(remoteGradleInvocation)

        then:
        1 * taskRepository.insert({Task task ->
            task.lastInvocation == converterService.date &&
            task.name == 'task2' &&
            task.nrOfInvocations == 1
        })
        1 * taskRepository.update({Task task ->
            task.lastInvocation == converterService.date &&
            task.name == 'task1' &&
            task.nrOfInvocations == 2
        })
    }

    def 'Create users in repository'(){
        given:
        'Remote invocation, repositories and service'()
        userRepository.containsName('userId') >> false

        when:
        converterService.saveInformationRegarding(remoteGradleInvocation)

        then:
        1 * userRepository.insert({User insertedUser ->
            insertedUser.lastInvocation == converterService.date &&
            insertedUser.nrOfInvocations == 1 &&
            insertedUser.userId == 'userId' &&
            insertedUser.tasks.get(0).name == 'task1' //&&
//            insertedUser.tasks.get(1).name == 'task2'
        })
    }

    def 'Update user in repository'(){
        given:
        'Remote invocation, repositories and service'()
        User user = Mock(User)
        userRepository.containsName('userId') >> true
        userRepository.withName('userId') >> user

        when:
        converterService.saveInformationRegarding(remoteGradleInvocation)

        then:
        1 * user.invoked(remoteGradleInvocation, _)

        then:
        1 * userRepository.update(user)
    }

    def 'Remote invocation, repositories and service'(){
        remoteGradleInvocation = new RemoteGradleInvocation()
        remoteGradleInvocation.commandLineTasks = ['task1', 'task2']
        remoteGradleInvocation.userId = "userId"
        gradleInvocationRepository = Mock(GradleInvocationRepository)
        userRepository = Mock(UserRepository)
        taskRepository = Mock(TaskRepository)
        statisticsRepository = Mock(StatisticsRepository)
        converterService = new RemoteInvocationConverterService(gradleInvocationRepository, userRepository,taskRepository,statisticsRepository)
    }

    def 'Saves statistics'(){
        // Not yet tested
    }
}
