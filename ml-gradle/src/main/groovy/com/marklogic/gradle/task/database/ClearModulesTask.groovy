package com.marklogic.gradle.task.database

import groovyx.net.http.HttpResponseDecorator;

import java.util.logging.Level
import java.util.logging.LogManager
import java.util.logging.Logger

import org.gradle.api.tasks.TaskAction

import com.marklogic.gradle.task.client.ClientTask
import com.marklogic.gradle.task.manage.ManageTask;
import com.marklogic.gradle.xcc.XccHelper

class ClearModulesTask extends ManageTask {

    String[] excludes

    // Used to check for existence of the XDBC server
    String groupId = "Default"

    /**
     * Should be able to optimize this when no excludes exist by clearing the forests belonging to the modules database.
     */
    @TaskAction
    void clearModules() {
        // Only try this if the XDBC server exists. If it doesn't, then we're probably doing a fresh install, so there's
        // no need to go any further.
        try {
            println "Checking to see if XDBC server exists..."
            invoke(getProject(), "GET", "/manage/v2/servers/" + getAppConfig().getName() + "-content-xdbc?group-id=" + groupId)
        } catch (Exception e) {
            println "No XDBC server exists yet, so not clearing modules\n"
            return
        }

        String xquery = "xdmp:eval(\"for \$uri in cts:uris((), (), cts:and-query(())) "
        if (excludes) {
            String excludesStr = excludes ? "('" + excludes.join("', '") + "')" : ""
            xquery += "where fn:not(\$uri = " + excludesStr + ") "
        }
        xquery += "return xdmp:document-delete(\$uri)\""
        xquery += ", (), <options xmlns='xdmp:eval'><database>{xdmp:modules-database()}</database></options>)"

        println "Clearing modules: " + xquery + "\n"
        new XccHelper(getDefaultXccUrl()).executeXquery(xquery)
    }
}
