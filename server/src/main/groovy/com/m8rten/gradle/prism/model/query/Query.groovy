package com.m8rten.gradle.prism.model.query

import com.m8rten.gradle.prism.model.Invocation
import com.m8rten.gradle.prism.model.invocation.InvocationEvent
import com.m8rten.gradle.prism.model.invocation.InvocationListener
import com.m8rten.gradle.prism.model.invocation.InvocationRepository

class Query implements InvocationListener {

    String name

    String mongoQuery

    Statistics statistics = new Statistics()

    List<Invocation> invocations

    String id = UUID.randomUUID().toString()

    private InvocationRepository repository

    private boolean hasBeenUpdated = true

    Query(){}

    Query(String name, String mongoQuery, InvocationRepository repository){
        this.name = name
        this.mongoQuery = mongoQuery
        this.repository = repository
        this.repository.addInvocationListener(this)
    }

    void run(){
        invocations = repository.run(mongoQuery)
        statistics.updateWith(invocations)
    }

    void changeAttributesToMatch(Query query) {
        this.name = query.name
        this.mongoQuery = query.mongoQuery
        this.hasBeenUpdated = true
    }

    void waitUntilUpdated(){
        hasBeenUpdated = true
        while(hasBeenUpdated){
            sleep(1000)
        }
    }

    @Override
    void invocationHasHappend(InvocationEvent event) {
        run()
        hasBeenUpdated = true
    }

    boolean hasBeenUpdated(){
        hasBeenUpdated
    }

    boolean waitForUpdate(){
        hasBeenUpdated = false
    }
}
