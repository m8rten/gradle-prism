package com.m8rten.gradle.prism.model

import org.mongojack.Id

class NrOfInvocationsForOneDay {
    @Id Date date
    int nrOfInvocations
}
