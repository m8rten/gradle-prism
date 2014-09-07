package com.m8rten.gradle.prism.model.query

import com.m8rten.gradle.prism.model.Invocation
import org.joda.time.DateTime

class StatisticsForTimePeriod {

    Map<Date, Integer> invocationsPerTimePeriod

    int invocationsForCurrentTimePeriod

    private timePeriodSelectionAlgorithm

    StatisticsForTimePeriod(){}

    def setAlgorithm(@DelegatesTo(strategy=Closure.DELEGATE_ONLY, value=DateTime) timePeriodSelectionAlgorithm){
        this.timePeriodSelectionAlgorithm = timePeriodSelectionAlgorithm
    }

    void updateWith(List<Invocation> invocations){
        invocationsForCurrentTimePeriod = 0
        Date modifiedCurrentDateTime = timePeriodSelectionAlgorithm.rehydrate(new DateTime(),this,this)().toDate()

        invocationsPerTimePeriod = [:]
        invocations.each {
            DateTime dateTimeForInvocation = new DateTime(it.time)
            def modifiedDayTime = timePeriodSelectionAlgorithm.rehydrate(dateTimeForInvocation, this, this)
            Date time = modifiedDayTime().toDate()
            if (invocationsPerTimePeriod.containsKey(time)){
                invocationsPerTimePeriod.put(time, invocationsPerTimePeriod.get(time) + 1)
            } else {
                invocationsPerTimePeriod.put(time,1)
            }
            if(modifiedCurrentDateTime.equals(time)){
                invocationsForCurrentTimePeriod++
            }
        }
    }
}
