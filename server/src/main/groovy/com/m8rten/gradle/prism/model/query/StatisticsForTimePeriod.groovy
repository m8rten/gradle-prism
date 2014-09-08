package com.m8rten.gradle.prism.model.query

import com.m8rten.gradle.prism.model.Invocation
import org.joda.time.DateTime

class StatisticsForTimePeriod {

    Map<Date, Integer> perTimePeriod

    int current

    private timePeriodSelectionAlgorithm

    StatisticsForTimePeriod(){}

    def setAlgorithm(@DelegatesTo(strategy=Closure.DELEGATE_ONLY, value=DateTime) timePeriodSelectionAlgorithm){
        this.timePeriodSelectionAlgorithm = timePeriodSelectionAlgorithm
    }

    void updateWith(List<Invocation> invocations){
        current = 0
        Date modifiedCurrentDateTime = timePeriodSelectionAlgorithm.rehydrate(new DateTime(),this,this)().toDate()

        perTimePeriod = [:]
        invocations.each {
            DateTime dateTimeForInvocation = new DateTime(it.time)
            def modifiedDayTime = timePeriodSelectionAlgorithm.rehydrate(dateTimeForInvocation, this, this)
            Date time = modifiedDayTime().toDate()
            if (perTimePeriod.containsKey(time)){
                perTimePeriod.put(time, perTimePeriod.get(time) + 1)
            } else {
                perTimePeriod.put(time,1)
            }
            if(modifiedCurrentDateTime.equals(time)){
                current++
            }
        }
    }
}
