package com.m8rten.gradle.prism.repository

import com.m8rten.gradle.prism.model.StatisticsForOneDay
import com.mongodb.DB
import org.mongojack.DBSort
import org.mongojack.JacksonDBCollection

class StatisticsRepository {

    final DB db
    JacksonDBCollection<StatisticsForOneDay, Date> statistics

    StatisticsRepository(DB db) {
        this.db = db
        statistics = JacksonDBCollection.wrap(db.getCollection("gradleInvocationsStatistics"), StatisticsForOneDay.class, Date.class)
    }

    StatisticsForOneDay withDate(Date day){
        statistics.findOneById(day)
    }

    boolean contains(Date day){
        statistics.findOneById(day) != null
    }

    void update(Date day, StatisticsForOneDay nrOfInvocationsForOneDay){
        statistics.updateById(day, nrOfInvocationsForOneDay)
    }

    List<StatisticsForOneDay> latest(int nr) {
        statistics.find().sort(DBSort.desc("\$natural")).limit(nr).toArray();
    }

    void add(StatisticsForOneDay nrOfInvocationsForOneDay) {
        statistics.insert(nrOfInvocationsForOneDay);
    }
}
