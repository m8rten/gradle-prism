package com.m8rten.gradle.prism.model.query
import com.m8rten.gradle.prism.model.invocation.InvocationRepository

class QueryContainer {

    final InvocationRepository repository

    private Map<String, Query> queries = new HashMap<String, Query>()

    QueryContainer(InvocationRepository repository) {
        this.repository = repository
    }

    Query add(Query queryTemplate){
        Query query = new Query(queryTemplate.name, queryTemplate.mongoQuery, repository)
        queries.put(query.id, query)
        query.run()
        query
    }

    Query update(Query queryTemplate){
        Query query = get(queryTemplate.id)
        query.changeAttributesToMatch(queryTemplate)
        query.run()
        query
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
