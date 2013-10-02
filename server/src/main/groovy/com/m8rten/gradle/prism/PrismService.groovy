package com.m8rten.gradle.prism

import com.m8rten.gradle.prism.boilerplate.MongoManaged
import com.m8rten.gradle.prism.boilerplate.PrismConfiguration
import com.m8rten.gradle.prism.repository.GradleInvocationRepository
import com.m8rten.gradle.prism.repository.StatisticsRepository
import com.m8rten.gradle.prism.repository.TaskRepository
import com.m8rten.gradle.prism.repository.UserRepository
import com.m8rten.gradle.prism.rest.*
import com.m8rten.gradle.prism.services.RemoteInvocationConverterService
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

        GradleInvocationRepository invocationRepository = new GradleInvocationRepository(db)
        UserRepository userRepository = new UserRepository(db)
        TaskRepository taskRepository = new TaskRepository(db)
        StatisticsRepository statisticsRepository = new StatisticsRepository(db)

        RemoteInvocationConverterService converterService = new RemoteInvocationConverterService(
                invocationRepository,
                userRepository,
                taskRepository,
                statisticsRepository)

        MongoManaged mongoManaged = new MongoManaged(mongo);
        environment.manage(mongoManaged);
        environment.addResource(new RemoteGradleInvocationResource(converterService: converterService));
        environment.addResource(new UsersResource(userRepository: userRepository))
        environment.addResource(new TasksResource(taskRepository: taskRepository))
        environment.addResource(new StatisticsResource(statisticsRepository: statisticsRepository))
        environment.addResource(new GradleInvocationResource(invocationRepository: invocationRepository))

    }
}