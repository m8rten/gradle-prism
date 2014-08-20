package com.m8rten.gradle.prism.query

import com.m8rten.gradle.prism.repository.InvocationRepository

class Query implements InvocationListener {

    String name

    String mongoQuery

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

    void changeAttributesFrom(Query query) {
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
