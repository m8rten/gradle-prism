package com.m8rten.gradle.prism.repository

import com.m8rten.gradle.prism.model.Task
import com.m8rten.gradle.prism.model.User
import com.mongodb.DB
import org.mongojack.DBSort
import org.mongojack.JacksonDBCollection

class UserRepository {

    final DB db

    final JacksonDBCollection<User, String> users

    UserRepository(DB db) {
        this.db = db
        users = JacksonDBCollection.wrap(db.getCollection("users"), User.class, String.class)
    }

    void update(String userId, List<Task> invokedTasks, Date currentDate) {
        User user = users.findOneById(userId)
        List<Task> taskHistory = user.tasks
        taskHistory.each {if (invokedTasks.contains(it)) it.nrOfInvocations++}
        taskHistory.addAll(invokedTasks - taskHistory)
        user.nrOfInvocations++
        user.lastInvocation = currentDate
        users.updateById(userId,user)
    }

    void insert(String userId, List<Task> invokedTasks, Date currentDate){
        users.insert(new User(userId: userId,
                nrOfInvocations: 1,
                tasks: invokedTasks,
                lastInvocation: currentDate))
    }

    boolean contains(String userId) {
        users.findOneById(userId) != null
    }

    List<User> getAll() {
//        users.find().toArray()
        users.find().sort(DBSort.desc("lastInvocation")).toArray();
    }
}
