package com.m8rten.gradle.prism.model.invocation

import com.m8rten.gradle.prism.model.invocation.InvocationEvent

public interface InvocationListener {
    void invocationHasHappend(InvocationEvent event)
}