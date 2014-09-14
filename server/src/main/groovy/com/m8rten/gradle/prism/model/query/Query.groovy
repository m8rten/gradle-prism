package com.m8rten.gradle.prism.model.query
import com.m8rten.gradle.prism.model.Invocation
import com.m8rten.gradle.prism.model.invocation.InvocationEvent
import com.m8rten.gradle.prism.model.invocation.InvocationListener
import com.m8rten.gradle.prism.model.invocation.InvocationRepository

class Query implements InvocationListener {

    String name

    String mongoQuery

    Statistics statistics = new Statistics()

    List<Invocation> invocations = []

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
        def newInvocations = repository.run(mongoQuery)
        /*
         * Improve this algorithm
         */
        if(newInvocations.size() != invocations.size()){
            invocations = newInvocations
            statistics.updateWith(invocations)
            hasBeenUpdated = true
        }
    }

    void changeAttributesToMatch(Query query) {
        this.name = query.name
        this.mongoQuery = query.mongoQuery
        this.hasBeenUpdated = true
    }

    @Override
    void invocationHasHappend(InvocationEvent event) {
        run()
    }

    boolean hasBeenUpdated(){
        hasBeenUpdated
    }

    boolean waitForUpdate(){
        hasBeenUpdated = false
    }
}
