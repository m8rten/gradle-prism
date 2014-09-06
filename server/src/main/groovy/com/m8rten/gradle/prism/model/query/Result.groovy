package com.m8rten.gradle.prism.model.query

import com.m8rten.gradle.prism.model.Invocation


class Result {

    List<Invocation> invocations

    void updateWith(List<Invocation> invocations) {
        this.invocations = invocations
    }
}
