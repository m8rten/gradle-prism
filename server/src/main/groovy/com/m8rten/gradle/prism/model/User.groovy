package com.m8rten.gradle.prism.model

import org.mongojack.Id

class User {
    @Id String userId
    List<Task> tasks
    int nrOfInvocations
    Date lastInvocation
}
