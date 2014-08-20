package com.m8rten.gradle.prism.query

import com.m8rten.gradle.prism.model.Invocation

class Result {

    GraphContainer graphContainer

    private List<Invocation> invocations

    void updateWith(List<Invocation> invocations) {
        this.invocations = invocations
    }

    List<Invocation> invocations(){
        invocations
    }
}
