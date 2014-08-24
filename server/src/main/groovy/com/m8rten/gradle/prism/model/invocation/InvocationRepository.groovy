package com.m8rten.gradle.prism.model.invocation

import com.m8rten.gradle.prism.model.invocation.Invocation
import com.m8rten.gradle.prism.model.invocation.InvocationEvent
import com.m8rten.gradle.prism.model.invocation.InvocationListener
import com.mongodb.DB
import groovy.beans.ListenerList
import org.jongo.Jongo
import org.jongo.MongoCollection

class InvocationRepository {

    final Jongo jongo

    final MongoCollection jongoInvocations

    @ListenerList
    List<InvocationListener> listeners

    InvocationRepository(DB db) {
        this.jongo = new Jongo(db);
        jongoInvocations = jongo.getCollection("superGradleInvocations");
    }

    void insert(Invocation invocation){
        jongoInvocations.save(invocation)
        fireInvocationHasHappend(new InvocationEvent())
    }

    List<Invocation> run(String query){
        def list = []
        jongoInvocations.find(query).as(Invocation.class).each {
            list << it
        }
        list
    }
}
