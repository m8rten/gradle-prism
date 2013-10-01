package com.m8rten.gradle.prism.repository
import com.m8rten.gradle.prism.model.GradleInvocation
import com.mongodb.DB
import org.mongojack.DBSort
import org.mongojack.JacksonDBCollection

class GradleInvocationRepository {

    final DB db

    final JacksonDBCollection<GradleInvocation, String> invocations

    GradleInvocationRepository(DB db) {
        this.db = db
        invocations = JacksonDBCollection.wrap(db.getCollection("gradleInvocations"), GradleInvocation.class, String.class)
    }

    void insert(GradleInvocation invocation){
        invocations.insert(invocation)
    }

    List<GradleInvocation> getInvocations(int nr){
        invocations.find().sort(DBSort.desc("\$natural")).limit(nr).toArray();
    }
}
