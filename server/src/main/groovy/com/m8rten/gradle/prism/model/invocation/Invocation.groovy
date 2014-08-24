package com.m8rten.gradle.prism.model.invocation

import com.m8rten.gradle.prism.model.RemoteGradleInvocation
import org.jongo.marshall.jackson.oid.Id

class Invocation extends RemoteGradleInvocation {


    Invocation(){}

    Invocation(RemoteGradleInvocation superRemoteGradleInvocation){
        projects = superRemoteGradleInvocation.projects
        user = user
    }

    @Id
    public String id

    Date time
}
