package com.m8rten.gradle.prism.rest

import com.m8rten.gradle.prism.query.GraphAttributes
import com.m8rten.gradle.prism.query.Query
import com.m8rten.gradle.prism.query.QueryContainer
import com.yammer.metrics.annotation.Timed

import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/query")
@Produces(MediaType.APPLICATION_JSON)
public class QueryResource {

    QueryContainer queries

    @POST
    @Consumes("application/json")
    public Response addOrUpdate(Query query) {
        Response.ok(queries.addOrUpdate(query), MediaType.APPLICATION_JSON).build()
    }


    @GET
    @Timed
    public Response queries() {
        Response.ok(queries.list(), MediaType.APPLICATION_JSON).build()
    }

    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("{uuid}")
    public Response delete(@PathParam("uuid") String id) {
        queries.delete(id)
        Response.ok().build()
    }

    @GET
    @Timed
    @Path("{uuid}")
    public Response getQuery(@PathParam("uuid") String id) {
        Response.ok(query(id), MediaType.APPLICATION_JSON).build()
    }

    @GET
    @Timed
    @Path("{uuid}/invocations")
    public Response invocations(@PathParam("uuid") String id) {
        Response.ok(query(id).result.invocations, MediaType.APPLICATION_JSON).build()
    }

    @GET
    @Timed
    @Path("{uuid}/waitUntilUpdated")
    public Response waitUntilUpdated(@PathParam("uuid") String id) {
        query(id).waitUntilUpdated()
        Response.ok().build()
    }

//    @GET
//    @Consumes("application/json")
//    @Path("{uuid}/graph")
//    public Response getGraphAttributes(@PathParam("queryUuid") String id) {
//        Response.ok(query(id).graphAttributes(), MediaType.APPLICATION_JSON).build()
//    }
//
//    @POST
//    @Consumes("application/json")
//    @Path("{queryUuid}/graph")
//    public Response addOrUpdateGraph(@PathParam("queryUuid") String id, GraphAttributes graphAttributes) {
//        Response.ok(query(id).createOrChangeFor(graphAttributes), MediaType.APPLICATION_JSON).build()
//    }
//
//    @GET
//    @Consumes("application/json")
//    @Path("{queryUuid}/graph/{graphUuid}/attributes")
//    public Response getGraphAttribute(@PathParam("queryUuid") String id, @PathParam("graphUuid") String graphId) {
//        Response.ok(query(id).graphAttributes(graphId), MediaType.APPLICATION_JSON).build()
//    }
//
//    @GET
//    @Consumes("application/json")
//    @Path("{queryUuid}/graph/{graphUuid}/data")
//    public Response getGraphData(@PathParam("queryUuid") String queryId,@PathParam("graphUuid") String graphId) {
//        Response.ok(query(queryId).graphData(graphId), MediaType.APPLICATION_JSON).build()
//    }

    Query query(String id){
        queries.get(id)
    }
}
