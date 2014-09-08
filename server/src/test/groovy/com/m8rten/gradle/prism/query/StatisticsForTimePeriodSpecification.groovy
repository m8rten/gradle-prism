package com.m8rten.gradle.prism.query
import com.m8rten.gradle.prism.model.Invocation
import com.m8rten.gradle.prism.model.query.StatisticsForTimePeriod
import org.joda.time.DateTime
import spock.lang.Specification

class StatisticsForTimePeriodSpecification extends Specification{

    def 'Can count invocations for current time period'(){
        given:
        def invocations = []
        StatisticsForTimePeriod statistics = new StatisticsForTimePeriod()
        statistics.setAlgorithm {
            withDayOfWeek(1).withTimeAtStartOfDay()
        }

        when:
        invocations << invocationWith(dateAtFirstDayOfWeek {plusHours 1})
        invocations << invocationWith(dateAtFirstDayOfWeek {plusHours 2})
        statistics.updateWith(invocations)

        then:
        statistics.current == 2

        when:
        invocations << invocationWith(dateAtFirstDayOfWeek {plusWeeks 3})
        statistics.updateWith(invocations)

        then:
        statistics.current == 2

        when:
        invocations << invocationWith(dateAtFirstDayOfWeek {plusDays 4})
        statistics.updateWith(invocations)

        then:
        statistics.current == 3
    }


        def 'Counts time correctly'(){
        given: 'Statistics that counts every DateTime depending on date'
        def invocations = []
        StatisticsForTimePeriod statistics = new StatisticsForTimePeriod()
        statistics.setAlgorithm {
            withTimeAtStartOfDay()
        }

        when: 'Two invocations that differs within two hours'
        invocations << invocationWith(dateAtFirstDayOfWeek {plusHours 1})
        invocations << invocationWith(dateAtFirstDayOfWeek {plusHours 2})
        statistics.updateWith(invocations)

        then: 'Then ...'
        statistics.perTimePeriod.size() == 1
        statistics.perTimePeriod.get(dateAtFirstDayOfWeek {plusHours 0}) == 2

        when:
        invocations << invocationWith(dateAtFirstDayOfWeek {plusDays 2})
        statistics.updateWith(invocations)

        then:
        statistics.perTimePeriod.size() == 2
        statistics.perTimePeriod.get(dateAtFirstDayOfWeek {plusHours 0}) == 2
        statistics.perTimePeriod.get(dateAtFirstDayOfWeek {plusDays 2}) == 1
    }

    def invocationWith(Date date) {
        def invocation = new Invocation()
        invocation.time = date
        invocation
    }

    def dateAtFirstDayOfWeek(@DelegatesTo(strategy=Closure.DELEGATE_ONLY, value=DateTime) cl){
        def dateTime = new DateTime().withDayOfWeek(1).withTimeAtStartOfDay()
        def code = cl.rehydrate(dateTime, this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code().toDate()
    }
}
