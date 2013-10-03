package com.m8rten.gradle.prism.model

import org.mongojack.Id

class StatisticsForOneDay {
    @Id Date date
    int nrOfInvocations = 0
    List<String> userNames = new ArrayList<String>()
    int nrOfUsers = 0

    public StatisticsForOneDay(Date date){
        this.date = date
    }

    public StatisticsForOneDay(){}

    public countInvocationBy(String userName){
        nrOfInvocations++
        if(!userNames.contains(userName))
            userNames.add(userName)
        nrOfUsers = userNames.size()
    }
}
