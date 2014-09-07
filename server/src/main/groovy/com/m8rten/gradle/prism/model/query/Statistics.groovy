package com.m8rten.gradle.prism.model.query
import com.m8rten.gradle.prism.model.Invocation

class Statistics {

    StatisticsForTimePeriod invocationsPerDay = new StatisticsForTimePeriod()
    StatisticsForTimePeriod invocationsPerWeek = new StatisticsForTimePeriod()
    StatisticsForTimePeriod invocationsPerMonth = new StatisticsForTimePeriod()

    void updateWith(List<Invocation> invocations) {
        invocationsPerDay.updateWith(invocations)
        invocationsPerMonth.updateWith(invocations)
        invocationsPerWeek.updateWith(invocations)
    }
}


