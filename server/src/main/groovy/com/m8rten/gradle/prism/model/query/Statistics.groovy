package com.m8rten.gradle.prism.model.query

class Statistics {

    Map<Date, Integer> invocationsPerDay = new HashMap<>()

    void updateWith(Result result) {
        invocationsPerDay = new HashMap<>()
        result.invocations.each {
            Date date = ((Date) it.time.clone()).clearTime()
            if (invocationsPerDay.containsKey(date)){
                invocationsPerDay.put(date, invocationsPerDay.get(date) + 1)
            } else {
                invocationsPerDay.put(date,0)
            }
        }
    }
}


