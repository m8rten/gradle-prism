package com.m8rten.gradle.prism.service
import com.m8rten.gradle.prism.model.GradleInvocation
import com.m8rten.gradle.prism.model.RemoteGradleInvocation
import com.m8rten.gradle.prism.model.StatisticsForOneDay
import com.m8rten.gradle.prism.model.Task
import com.m8rten.gradle.prism.model.User
import com.m8rten.gradle.prism.repository.GradleInvocationRepository
import com.m8rten.gradle.prism.repository.StatisticsRepository
import com.m8rten.gradle.prism.repository.TaskRepository
import com.m8rten.gradle.prism.repository.UserRepository

class RemoteInvocationConverterService {

    Date date

    private final GradleInvocationRepository gradleInvocations

    private final UserRepository users

    private final TaskRepository tasks

    private final StatisticsRepository statistics

    RemoteInvocationConverterService(GradleInvocationRepository gradleInvocations, UserRepository users, TaskRepository tasks, StatisticsRepository statistics) {
        this.gradleInvocations = gradleInvocations
        this.users = users
        this.tasks = tasks
        this.statistics = statistics
    }

    synchronized void saveInformationRegarding(RemoteGradleInvocation remoteGradleInvocation) {
        date = new Date()
        saveInvocationInformation(remoteGradleInvocation)
        saveTaskInformation(remoteGradleInvocation)
        saveUserInformation(remoteGradleInvocation)
        updateStatistics(remoteGradleInvocation)
    }


    /**
     * Tasks
     */
    private void saveTaskInformation(RemoteGradleInvocation remoteGradleInvocation) {
        remoteGradleInvocation.commandLineTasks.each { String taskName ->
            if (tasks.containsName(taskName)) {
                Task task = tasks.withName(taskName)
                task.wasRunAt(date)
                tasks.update(task)
            } else {
                Task task = new Task(taskName)
                task.wasRunAt(date)
                tasks.insert(task)
            }
        }
    }

    /**
     * Users
     */
    private void saveUserInformation(RemoteGradleInvocation remoteGradleInvocation) {
        if (users.containsName(remoteGradleInvocation.userId)){
            User user = users.withName(remoteGradleInvocation.userId)
            user.invoked(remoteGradleInvocation, date)
            users.update(user)
        }  else {
            User user = new User(remoteGradleInvocation.userId)
            user.invoked(remoteGradleInvocation, date)
            users.insert(user)
        }
    }

    /**
     * Gradle invocations
     */
    private void saveInvocationInformation(RemoteGradleInvocation remoteGradleInvocation) {
        gradleInvocations.insert(new GradleInvocation(remoteGradleInvocation,date))
    }

    /**
     * Gradle stats
     */
    private void updateStatistics(RemoteGradleInvocation remoteGradleInvocation){
        Date day = (Date) date.clone()
        day.clearTime()
        if(statistics.contains(day)){
            StatisticsForOneDay invocationsForOneDay = statistics.withDate(day)
            invocationsForOneDay.countInvocationBy(remoteGradleInvocation.userId)
            statistics.update(day, invocationsForOneDay)

        } else {
            StatisticsForOneDay invocationsForOneDay = new StatisticsForOneDay(day)
            invocationsForOneDay.countInvocationBy(remoteGradleInvocation.userId)
            statistics.add(invocationsForOneDay)
        }
    }
}
