package com.m8rten.gradle.prism.repository
import com.m8rten.gradle.prism.model.Task
import com.mongodb.DB
import org.mongojack.DBSort
import org.mongojack.JacksonDBCollection

class TaskRepository {
    final DB db

    final JacksonDBCollection<Task, String> tasks

    TaskRepository(DB db) {
        this.db = db
        tasks = JacksonDBCollection.wrap(db.getCollection("tasks"), Task.class, String.class)
    }

    void update(Task task) {
        task.nrOfInvocations += tasks.findOneById(task.name).nrOfInvocations
        tasks.updateById(task.name,task)
    }

    void insert(Task task){
        tasks.insert(task)
    }

    boolean contains(Task task) {
        contains(task.name)
    }

    List<Task> getAll() {
        tasks.find().sort(DBSort.desc("lastInvocation")).toArray();
    }

    Task get(String name) {
        tasks.findOneById(name)
    }

    boolean contains(String taskName){
        tasks.findOneById(taskName) != null
    }
}
