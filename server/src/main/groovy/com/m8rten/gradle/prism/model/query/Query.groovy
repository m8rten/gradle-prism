package com.m8rten.gradle.prism.model.query

import com.m8rten.gradle.prism.model.invocation.InvocationEvent
import com.m8rten.gradle.prism.model.invocation.InvocationListener
import com.m8rten.gradle.prism.model.invocation.InvocationRepository

class Query implements InvocationListener {

    String name

    String mongoQuery

    //Statistics statistics = new Statistics()

    Result result = new Result()

    String id = UUID.randomUUID().toString()

    private InvocationRepository repository

    private boolean noNewInvocations = true

    Query(){}

    Query(String name, String mongoQuery, InvocationRepository repository){
        this.name = name
        this.mongoQuery = mongoQuery
        this.repository = repository
        this.repository.addInvocationListener(this)
    }

    void run(){
        result.updateWith(repository.run(mongoQuery))
    }

    void changeAttributesToMatch(Query query) {
        this.name = query.name
        this.mongoQuery = query.mongoQuery
    }

    void waitUntilUpdated(){
        noNewInvocations=true
        while(noNewInvocations){
            sleep(1000)
        }
    }

    @Override
    void invocationHasHappend(InvocationEvent event) {
        run()
        noNewInvocations=false
    }
}
