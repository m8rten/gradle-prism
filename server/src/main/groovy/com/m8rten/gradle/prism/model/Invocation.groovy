package com.m8rten.gradle.prism.model

import org.jongo.marshall.jackson.oid.Id

class Invocation {

    @Id
    public String id
    SuperUser user
    List<SuperProject> projects
    Date time
}
