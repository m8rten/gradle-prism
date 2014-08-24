package com.m8rten.gradle.prism.query

import com.m8rten.gradle.prism.model.invocation.InvocationRepository
import com.m8rten.gradle.prism.model.query.QueryContainer
import spock.lang.Specification

class QueryContainerSpecification extends Specification{

    def "Creates query with UUID"(){
        given:
        def repository = Mock(InvocationRepository)
        QueryContainer queryContainer = new QueryContainer(repository)
        def question = Mock(Conditions)

        expect:
        queryContainer.isEmpty()

        when:
        UUID uuid = queryContainer.addOrUpdate(question)

        then:
        queryContainer.contains(uuid)
    }

    def "Creates query with question and repository "(){
        given:
        def repository = Mock(InvocationRepository)
        def queryContainer = new QueryContainer(repository)
        def question = Mock(Conditions)

        when:
        UUID uuid = queryContainer.addOrUpdate(question)
        println uuid

        then:
        queryContainer.get(uuid).conditions == question
        queryContainer.get(uuid).repository == repository
    }

    def "Add repository to query container"(){
        given:
        def repository = Mock(InvocationRepository)
        def queryContainer = new QueryContainer(repository)

        expect:
        queryContainer.repository == repository
    }
}
