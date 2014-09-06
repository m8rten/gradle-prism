package com.m8rten.gradle.prism

import com.m8rten.gradle.prism.boilerplate.MongoManaged
import com.m8rten.gradle.prism.boilerplate.PrismConfiguration
import com.m8rten.gradle.prism.model.invocation.InvocationRepository
import com.m8rten.gradle.prism.model.query.QueryContainer
import com.m8rten.gradle.prism.rest.QueryResource
import com.m8rten.gradle.prism.rest.RemoteGradleInvocationResource
import com.mongodb.DB
import com.mongodb.Mongo
import io.dropwizard.Application
import io.dropwizard.assets.AssetsBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment

public class Prism extends Application<PrismConfiguration> {

    public static void main(String[] args) throws Exception {
        new Prism().run(args);
    }

    @Override
    public void initialize(Bootstrap<PrismConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.html"))
    }

    @Override
    public void run(PrismConfiguration configuration, Environment environment) {
        Mongo mongo = new Mongo(configuration.mongohost, configuration.mongoport)
        DB db = mongo.getDB(configuration.mongodb);

        InvocationRepository superInvocations = new InvocationRepository(db)

        QueryContainer containter = new QueryContainer(superInvocations)

        MongoManaged mongoManaged = new MongoManaged(mongo);
        environment.lifecycle().manage(mongoManaged);
        environment.jersey().setUrlPattern("/api/*");
        environment.jersey().register(new RemoteGradleInvocationResource(invocations: superInvocations))
        environment.jersey().register(new QueryResource(queries: containter))
    }
}