package com.m8rten.gradle.prism
import com.bazaarvoice.dropwizard.assets.ConfiguredAssetsBundle
import com.m8rten.gradle.prism.boilerplate.MongoManaged
import com.m8rten.gradle.prism.boilerplate.PrismConfiguration
import com.m8rten.gradle.prism.model.query.QueryContainer
import com.m8rten.gradle.prism.model.invocation.InvocationRepository
import com.m8rten.gradle.prism.rest.QueryResource
import com.m8rten.gradle.prism.rest.RemoteGradleInvocationResource
import com.mongodb.DB
import com.mongodb.Mongo
import com.yammer.dropwizard.Service
import com.yammer.dropwizard.config.Bootstrap
import com.yammer.dropwizard.config.Environment

public class PrismService extends Service<PrismConfiguration> {

    public static void main(String[] args) throws Exception {
        new PrismService().run(args);
    }

    @Override
    public void initialize(Bootstrap<PrismConfiguration> bootstrap) {
        bootstrap.setName("gradle-prism");
        bootstrap.addBundle(new ConfiguredAssetsBundle("/assets/", "/"))
    }

    @Override
    public void run(PrismConfiguration configuration, Environment environment) {
        Mongo mongo = new Mongo(configuration.mongohost, configuration.mongoport)
        DB db = mongo.getDB(configuration.mongodb);

        InvocationRepository superInvocations = new InvocationRepository(db)

        QueryContainer containter = new QueryContainer(superInvocations)

        MongoManaged mongoManaged = new MongoManaged(mongo);
        environment.manage(mongoManaged);
        environment.addResource(new RemoteGradleInvocationResource(invocations: superInvocations))
        environment.addResource(new QueryResource(queries: containter))
    }
}