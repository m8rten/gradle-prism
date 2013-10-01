package com.m8rten.gradle.prism.services
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

    private final GradleInvocationRepository gradleInvocationRepository

    private final UserRepository userRepository

    private final TaskRepository taskRepository

    private final StatisticsRepository statisticsRepository

    RemoteInvocationConverterService(GradleInvocationRepository gradleInvocationRepository, UserRepository userRepository, TaskRepository taskRepository, StatisticsRepository statisticsRepository) {
        this.gradleInvocationRepository = gradleInvocationRepository
        this.userRepository = userRepository
        this.taskRepository = taskRepository
        this.statisticsRepository = statisticsRepository
    }

    void save(RemoteGradleInvocation remoteGradleInvocation) {
        date = new Date()
        saveInvocation(remoteGradleInvocation)
        saveTasks(remoteGradleInvocation)
        saveUser(remoteGradleInvocation)
        saveStatistics(remoteGradleInvocation)
    }


    /**
     * Tasks
     */
    private void saveTasks(RemoteGradleInvocation remoteGradleInvocation) {
        remoteGradleInvocation.commandLineTasks.each { String taskName ->
            saveTask(taskName)
        }
    }
    private void saveTask(String taskName) {
        if (taskRepository.contains(taskName)) {
            Task task = taskRepository.get(taskName)
            task.lastInvocation = date
            task.nrOfInvocations = task.nrOfInvocations + 1
            taskRepository.update(task)
        } else {
            taskRepository.insert(new Task(taskName, date))
        }
    }

    /**
     * Users
     */
    private void saveUser(RemoteGradleInvocation remoteGradleInvocation) {
        if (userRepository.contains(remoteGradleInvocation.userId)){
            updateUser(remoteGradleInvocation)
        }  else {
            createUser(remoteGradleInvocation)
        }
    }
    private void createUser(RemoteGradleInvocation remoteGradleInvocation) {
        User user = new User(remoteGradleInvocation.userId, date)
        user.tasks = remoteGradleInvocation.commandLineTasks.collect({ new Task(it, date) })
        userRepository.insert(user)
    }
    private void updateUser(RemoteGradleInvocation remoteGradleInvocation) {
        User user = userRepository.get(remoteGradleInvocation.userId)
        user.lastInvocation = date
        user.nrOfInvocations++
        user.tasks.findAll {remoteGradleInvocation.commandLineTasks.contains(it.name)}.each{
            it.lastInvocation = date
            it.nrOfInvocations++
        }
        remoteGradleInvocation.commandLineTasks.each { String taskName ->
            if (user.tasks.find { it.name == taskName } == null)
                user.tasks.add(new Task(taskName, date))
        }
        userRepository.update(user)
    }

    /**
     * Gradle invocations
     */
    private void saveInvocation(RemoteGradleInvocation remoteGradleInvocation) {
        GradleInvocation invocation = new GradleInvocation();
        invocation.userId = remoteGradleInvocation.userId
        invocation.commandLineTasks = remoteGradleInvocation.commandLineTasks
        invocation.date = date
        gradleInvocationRepository.insert(invocation)
    }

    /**
     * Gradle stats
     */
    private void saveStatistics(RemoteGradleInvocation remoteGradleInvocation){
        Date day = (Date) date.clone()
        day.clearTime()
        if(statisticsRepository.contains(day)){
            StatisticsForOneDay invocationsForOneDay = statisticsRepository.get(day)
            invocationsForOneDay.addInvocation(remoteGradleInvocation.userId)
            statisticsRepository.update(day, invocationsForOneDay)

        } else {
            StatisticsForOneDay invocationsForOneDay = new StatisticsForOneDay(day)
            invocationsForOneDay.addInvocation(remoteGradleInvocation.userId)
            statisticsRepository.add(invocationsForOneDay)
        }
    }
}
