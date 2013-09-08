package com.m8rten.gradle.prism.model

import org.mongojack.Id

class GradleInvocation {
    @Id public String id;
    String userId
    String commandLineTasks
    Date date
}
