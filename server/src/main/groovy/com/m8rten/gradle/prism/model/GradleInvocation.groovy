package com.m8rten.gradle.prism.model

import org.mongojack.Id

class GradleInvocation {
    @Id public String id;
    String userId
    List<String> commandLineTasks
    Date date
}
