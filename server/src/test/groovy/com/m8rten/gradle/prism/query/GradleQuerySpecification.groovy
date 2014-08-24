package com.m8rten.gradle.prism.query

import com.m8rten.gradle.prism.model.invocation.Invocation
import com.m8rten.gradle.prism.model.query.Query
import com.m8rten.gradle.prism.model.invocation.InvocationRepository
import com.m8rten.gradle.prism.model.query.QueryContainer

//import org.mongojack.DBQuery
import spock.lang.Specification

class GradleQuerySpecification extends Specification{


    def "Saves output from query"(){
        given:
        def question = Mock(Conditions)
        def repository = Mock(InvocationRepository)
        def gradleQuery = new Query(question,repository)
        def invocation = Mock(Invocation)
        List<Invocation> invocations = [invocation]
//        1 * repository.doQuery(_) >> invocations

        expect:
        gradleQuery.invocations().empty

        when:
        gradleQuery.loadInvocations()

        then:
        gradleQuery.invocations().size() == 1
        gradleQuery.invocations().get(0) == invocation
    }

    def "Queries repository for user"(){
        given:
        def question = Mock(Conditions)
        def repository = Mock(InvocationRepository)
        def gradleQuery = new Query(question,repository)
//        def mongoDbQuery = DBQuery.empty()
//        1 * question.mongoStyledQuery() >> mongoDbQuery
//
//        when:
//        gradleQuery.update()

//        then:
//        1 * repository.doQuery(mongoDbQuery)
    }

    def "Creates query with question and repository "(){
        given:
        def repository = Mock(InvocationRepository)
        def queryContainer = new QueryContainer(repository)
        def question = Mock(Conditions)

        when:
        UUID uuid = queryContainer.addOrUpdate(question)

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
