package com.m8rten.gradle.prism.services

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
        converterService.save(remoteGradleInvocation)

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
        taskRepository.contains('task1') >> true
        taskRepository.get('task1') >> new Task('task1',null)
        taskRepository.contains('task2') >> false

        when:
        converterService.save(remoteGradleInvocation)

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
        userRepository.contains('userId') >> false

        when:
        converterService.save(remoteGradleInvocation)

        then:
        1 * userRepository.insert({User insertedUser ->
            insertedUser.lastInvocation == converterService.date &&
            insertedUser.nrOfInvocations == 1 &&
            insertedUser.userId == 'userId' &&
            insertedUser.tasks.get(0).name == 'task1' &&
            insertedUser.tasks.get(1).name == 'task2'
        })
    }

    def 'Update user in repository'(){
        given:
        'Remote invocation, repositories and service'()

        when:
        userRepository.contains('userId') >> true
        userRepository.get('userId') >> new User('userId',null)
        converterService.save(remoteGradleInvocation)

        then:
        1 * userRepository.update({User user ->
            user.lastInvocation == converterService.date &&
            user.nrOfInvocations == 2 &&
            user.userId == 'userId'
        })
    }

    def 'Update tasks in user'(){
        given:
        'Remote invocation, repositories and service'()

        when:
        userRepository.contains('userId') >> true
        User user = new User('userId',null)
        user.tasks = [new Task('task1',null)]
        userRepository.get('userId') >> user
        converterService.save(remoteGradleInvocation)

        then:
        1 * userRepository.update({User insertedUser ->
            insertedUser.userId == 'userId' &&
            insertedUser.tasks.get(0).name == 'task1' &&
            insertedUser.tasks.get(0).nrOfInvocations == 2 &&
            insertedUser.tasks.get(0).lastInvocation == converterService.date &&
            insertedUser.tasks.get(1).name == 'task2' &&
            insertedUser.tasks.get(1).nrOfInvocations == 1 &&
            insertedUser.tasks.get(1).lastInvocation == converterService.date
        })
    }

    def 'Remote invocation, repositories and service'(){
        remoteGradleInvocation = Mock(RemoteGradleInvocation)
        remoteGradleInvocation.getCommandLineTasks() >> ['task1', 'task2']
        remoteGradleInvocation.getUserId() >> "userId"
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
