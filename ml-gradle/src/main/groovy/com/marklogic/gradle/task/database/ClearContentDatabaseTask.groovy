package com.marklogic.gradle.task.database

import org.gradle.api.tasks.TaskAction

import com.marklogic.gradle.task.manage.ManageTask
import com.marklogic.gradle.xcc.XccHelper

class ClearContentDatabaseTask extends ManageTask {

    @TaskAction
    void clearModules() {
        if (!xdbcServerExists()) {
            println "No XDBC server exists yet, so not clearing modules\n"
            return
        }

        String xquery = "for \$forest-id in xdmp:database-forests(xdmp:database()) return xdmp:forest-clear(\$forest-id)"
        println "Clearing modules: " + xquery + "\n"
        new XccHelper(getDefaultXccUrl()).executeXquery(xquery)
    }
}
