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
            queries.get(queryTemplate.id).changeAttributesFrom(queryTemplate)
            get(queryTemplate.id)
        } else {
            Query query = new Query(queryTemplate.name, queryTemplate.mongoQuery, repository)
            queries.put(query.id, query)
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
