package com.m8rten.gradle.prism.model.query
import com.m8rten.gradle.prism.model.Invocation

class Statistics {

    Statistics(){
        invocationsPerDay.setAlgorithm {
            withTimeAtStartOfDay()
        }
        invocationsPerWeek.setAlgorithm {
            withTimeAtStartOfDay().withDayOfWeek(1)
        }
        invocationsPerMonth.setAlgorithm {
            withTimeAtStartOfDay().withDayOfMonth(1)
        }
    }

    StatisticsForTimePeriod invocationsPerDay = new StatisticsForTimePeriod()
    StatisticsForTimePeriod invocationsPerWeek = new StatisticsForTimePeriod()
    StatisticsForTimePeriod invocationsPerMonth = new StatisticsForTimePeriod()

    void updateWith(List<Invocation> invocations) {
        invocationsPerDay.updateWith(invocations)
        invocationsPerMonth.updateWith(invocations)
        invocationsPerWeek.updateWith(invocations)
    }
}


