package com.m8rten.gradle.prism.rest
import com.fasterxml.jackson.databind.util.JSONPObject
import com.m8rten.gradle.prism.repository.StatisticsRepository
import com.yammer.metrics.annotation.Timed

import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/invocations/statistics/{nr}")
@Produces(MediaType.APPLICATION_JSON)
public class StatisticsResource {

    StatisticsRepository statisticsRepository

    @GET
    @Timed
    public JSONPObject getInvocations(@QueryParam("callback") String callback, @PathParam("nr") int nr) {
        new JSONPObject(callback, statisticsRepository.getInvocationStatistics(nr))
    }
}
