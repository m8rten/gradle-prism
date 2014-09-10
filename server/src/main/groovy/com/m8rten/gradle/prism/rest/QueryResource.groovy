package com.m8rten.gradle.prism.rest

import com.codahale.metrics.annotation.Timed
import com.m8rten.gradle.prism.model.query.Query
import com.m8rten.gradle.prism.model.query.QueryContainer

import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/query")
@Produces(MediaType.APPLICATION_JSON)
public class QueryResource {

    QueryContainer queries

    @POST
    @Consumes("application/json")
    public Response add(Query query) {
        Response.ok(queries.add(query), MediaType.APPLICATION_JSON).build()
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
    @Path("{uuid}")
    public Response getQuery(@PathParam("uuid") String id) {
        Response.ok(query(id), MediaType.APPLICATION_JSON).build()
    }

    @GET
    @Path("{uuid}/invocations")
    public Response invocations(@PathParam("uuid") String id) {
        Response.ok(query(id).invocations, MediaType.APPLICATION_JSON).build()
    }

    @GET
    @Path("waitUntilUpdated")
    public Response waitUntilUpdated() {
        Response.ok(queries.waitUntilUpdated(), MediaType.APPLICATION_JSON).build()
    }

    @POST
    @Path("{uuid}")
    public Response update(@PathParam("uuid") String id, Query query) {
        Response.ok(queries.update(query), MediaType.APPLICATION_JSON).build()
    }

    Query query(String id){
        queries.get(id)
    }
}
