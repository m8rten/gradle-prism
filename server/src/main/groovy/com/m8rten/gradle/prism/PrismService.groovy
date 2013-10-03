package com.m8rten.gradle.prism

import com.m8rten.gradle.prism.boilerplate.MongoManaged
import com.m8rten.gradle.prism.boilerplate.PrismConfiguration
import com.m8rten.gradle.prism.repository.GradleInvocationRepository
import com.m8rten.gradle.prism.repository.StatisticsRepository
import com.m8rten.gradle.prism.repository.TaskRepository
import com.m8rten.gradle.prism.repository.UserRepository
import com.m8rten.gradle.prism.rest.*
import com.m8rten.gradle.prism.service.RemoteInvocationConverterService
import com.mongodb.DB
import com.mongodb.Mongo
import com.yammer.dropwizard.Service
import com.yammer.dropwizard.assets.AssetsBundle
import com.yammer.dropwizard.config.Bootstrap
import com.yammer.dropwizard.config.Environment

public class PrismService extends Service<PrismConfiguration> {

    public static void main(String[] args) throws Exception {
        new PrismService().run(args);
    }

    @Override
    public void initialize(Bootstrap<PrismConfiguration> bootstrap) {
        bootstrap.setName("gradle-prism");
        bootstrap.addBundle(new AssetsBundle("/assets/", "/"))
    }

    @Override
    public void run(PrismConfiguration configuration, Environment environment) {

        Mongo mongo = new Mongo(configuration.mongohost, configuration.mongoport)
        DB db = mongo.getDB(configuration.mongodb);

        GradleInvocationRepository invocations = new GradleInvocationRepository(db)
        UserRepository users = new UserRepository(db)
        TaskRepository tasks = new TaskRepository(db)
        StatisticsRepository statistics = new StatisticsRepository(db)

        RemoteInvocationConverterService converter = new RemoteInvocationConverterService(
                invocations,
                users,
                tasks,
                statistics)

        MongoManaged mongoManaged = new MongoManaged(mongo);
        environment.manage(mongoManaged);
        environment.addResource(new RemoteGradleInvocationResource(converter: converter));
        environment.addResource(new UsersResource(users: users))
        environment.addResource(new TasksResource(tasks: tasks))
        environment.addResource(new StatisticsResource(statistics: statistics))
        environment.addResource(new GradleInvocationResource(invocations: invocations))
    }
}