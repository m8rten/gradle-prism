package com.m8rten.gradle.prism.plugin
import com.m8rten.gradle.prism.model.RemoteGradleInvocation
import groovyx.net.http.RESTClient
import org.gradle.BuildResult
import org.gradle.StartParameter
import org.gradle.api.Project
import org.gradle.api.invocation.Gradle
import spock.lang.Specification

class SpySpecification extends Specification {

    def gradleInvocation = Mock(RemoteGradleInvocation)

    def restClient = Mock(RESTClient)

    def buildResult = Mock(BuildResult)

    def project = Mock(Project)

    def gradle = Mock(Gradle)

    Spy spy


    def 'Posts gradle invocations when build is finished'(){
        given:
        'Spy with rest client, gradle invocation and ruild result'()

        when:
        spy.buildFinished(buildResult)

        then:
        1 * restClient.setUri('test_url')
        1 * gradleInvocation.setCommandLineTasks("task1, task2")
        1 * gradleInvocation.setUserId(System.getProperty('user.name'))

        then:
        1 * restClient.post([body:gradleInvocation])
    }

    def "Doesn't post gradle invocation when build is finished when user name is filtered"(){
        given:
        'Spy with rest client, gradle invocation and ruild result'()

        and:
        spy.filter(System.getProperty('user.name'))

        when:
        spy.buildFinished(buildResult)

        then:
        0 * restClient.post(_)
    }

    def "Crashes silently aka doesn't revel itself when mission has failed"(){
        given:
        'Spy with rest client, gradle invocation and ruild result'()

        and: 'Using real RESTClient but with fucked up url'
        spy.restClient = new RESTClient()

        when:
        spy.buildFinished(buildResult)

        then:
        noExceptionThrown()
    }

    def 'Spies at build'(){
        given:

        when:
        'Spy with rest client, gradle invocation and ruild result'()

        then:
        /* TODO: SHOULD BE SPY INSTANCE SPECIFIC */
        1 * gradle.addBuildListener(_)
    }

    def 'Spy with rest client, gradle invocation and ruild result'(){
        def startParameter = Mock(StartParameter)
        startParameter.getTaskNames() >> ['task1','task2']
        gradle.getStartParameter() >> startParameter
        buildResult.getGradle() >> gradle
        project.getGradle() >> gradle
        spy = new Spy(project)
        spy.url = 'test_url'
        spy.restClient = restClient
        spy.gradleInvocation = gradleInvocation
    }

//    def 'Integration Test'(){
//        given:
//        spy.url = 'http://localhost:8080/api/invocation/'
//
//        def startParameter = Mock(StartParameter)
//        startParameter.getTaskNames() >> ['task100','task2']
//
//        def gradle = Mock(Gradle)
//        gradle.getStartParameter() >> startParameter
//
//        buildResult.getGradle() >> gradle
//
//        when:
//        spy.buildFinished(buildResult)
//
//        then:
//        true
//    }

}
