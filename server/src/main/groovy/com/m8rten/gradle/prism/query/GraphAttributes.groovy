package com.m8rten.gradle.prism.query

import com.m8rten.gradle.prism.model.Invocation

class GraphAttributes {

    String name
    String type
    String id = UUID.randomUUID().toString()

    Map<Date,Integer> calculateDataFor(List<Invocation> invocations){
        Map<Date, Integer> data = new HashMap<Date,Integer>();
        if(type.equals("day")){
            invocations.each {
                Date day = it.time.clearTime()
                if(data.containsKey(day)){
                    data.get(day)++
                } else {
                    data.put(day,1)
                }
            }
        }
        return data
    }
}
