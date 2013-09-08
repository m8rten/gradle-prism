package com.m8rten.gradle.prism.repository

import com.mongodb.DB
import org.mongojack.DBSort
import org.mongojack.JacksonDBCollection
import com.m8rten.gradle.prism.model.NrOfInvocationsForOneDay
import com.m8rten.gradle.prism.model.GradleInvocation

class GradleInvocationRepository {

    final DB db
    final JacksonDBCollection<GradleInvocation, String> invocations
    JacksonDBCollection<NrOfInvocationsForOneDay, Date> statistics

    GradleInvocationRepository(DB db) {
        this.db = db
        invocations = JacksonDBCollection.wrap(db.getCollection("gradleInvocations"), GradleInvocation.class, String.class)
        statistics = JacksonDBCollection.wrap(db.getCollection("gradleInvocationsStatistics"), NrOfInvocationsForOneDay.class, Date.class)

    }

    void insert(GradleInvocation invocation){
        invocations.insert(invocation)
        Date day = (Date) invocation.date.clone()
        day.clearTime()
        NrOfInvocationsForOneDay stat = statistics.findOneById(day)
        if(stat == null){
            statistics.insert(new NrOfInvocationsForOneDay(date: day, nrOfInvocations: 1))
        } else {
            stat.nrOfInvocations++;
            statistics.updateById(day,stat)
        }
    }

    List<GradleInvocation> getInvocations(int nr){
        invocations.find().sort(DBSort.desc("\$natural")).limit(nr).toArray();
    }

    List<NrOfInvocationsForOneDay> getInvocationStatistics(int nr) {
        statistics.find().sort(DBSort.desc("\$natural")).limit(nr).toArray();
    }
}
