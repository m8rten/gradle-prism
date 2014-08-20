package com.m8rten.gradle.prism.query

import com.m8rten.gradle.prism.repository.InvocationRepository

class QueryContainer {

    final InvocationRepository repository

    private Map<String, Query> queries = new HashMap<String, Query>()

    QueryContainer(InvocationRepository repository) {
        this.repository = repository
    }

    Query addOrUpdate(Query queryTemplate) {
        if(contains(queryTemplate.id)){
            Query query = get(queryTemplate.id)
            query.changeAttributesFrom(queryTemplate)
            query.run()
            query
        } else {
            Query query = new Query(queryTemplate.name, queryTemplate.mongoQuery, repository)
            queries.put(query.id, query)
            query.run()
            query
        }
    }

    Query delete(String id) {
        queries.remove(id)
    }

    boolean contains(String uuid) {
        queries.containsKey(uuid)
    }

    Query get(String id) {
        queries.get(id)
    }

    boolean isEmpty() {
        queries.isEmpty()
    }

    List<Query> list(){
        queries.values().asList()
    }
}
