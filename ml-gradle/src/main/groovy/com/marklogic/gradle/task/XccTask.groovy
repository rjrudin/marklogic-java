package com.marklogic.gradle.task

import org.gradle.api.tasks.TaskAction

import com.marklogic.gradle.MarkLogicTask
import com.marklogic.gradle.xcc.XccHelper

/**
 * Simple task for easing the execution of ad hoc XQuery.
 */
class XccTask extends MarkLogicTask {

    String xquery

    @TaskAction
    void executeXcc() {
        new XccHelper(getDefaultXccUrl()).executeXquery(xquery)
    }
}
