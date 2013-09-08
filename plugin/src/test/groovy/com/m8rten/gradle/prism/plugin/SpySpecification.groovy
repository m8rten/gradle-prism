package com.m8rten.gradle.prism.plugin

import com.m8rten.gradle.prism.model.RemoteGradleInvocation
import groovyx.net.http.RESTClient
import org.gradle.BuildResult
import org.gradle.StartParameter
import org.gradle.api.invocation.Gradle
import spock.lang.Specification

class SpySpecification extends Specification {

    def gradleInvocation = Mock(RemoteGradleInvocation)

    def restClient = Mock(RESTClient)

    def buildResult = Mock(BuildResult)

    def spy = new Spy()

    def 'Posts a Gradle Invocation'(){
        given:
        'Spy with RESTClient and gradle invocation'()

        when:
        spy.buildFinished(buildResult)

        then:
        1 * restClient.setUri('test_url')
        1 * gradleInvocation.setCommandLineTasks("task1, task2")
        1 * gradleInvocation.setUserId(System.getProperty('user.name'))

        then:
        1 * restClient.post([path: 'asdf', body:gradleInvocation])
    }


    def "Doesn't post gradle invocation if user name is filtered"(){
        given:
        'Spy with RESTClient and gradle invocation'()

        and:
        spy.filter = [System.getProperty('user.name')]

        when:
        spy.buildFinished(buildResult)

        then:
        0 * restClient.post(_)
    }

    def "Spy with RESTClient and gradle invocation"(){
        spy.url = 'test_url'
        spy.restClient = restClient
        spy.gradleInvocation = gradleInvocation

        def startParameter = Mock(StartParameter)
        startParameter.getTaskNames() >> ['task1','task2']

        def gradle = Mock(Gradle)
        gradle.getStartParameter() >> startParameter

        buildResult.getGradle() >> gradle
    }
}
