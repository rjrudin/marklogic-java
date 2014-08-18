package com.marklogic.gradle.task.security

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import com.marklogic.gradle.xcc.XccHelper

class RemoveUsersTask extends DefaultTask {

    String xccUrl
    String[] usernames

    @TaskAction
    void executeXquery() {
        String usernamesStr = "'" + usernames.join("', '") + "'"

        String xquery = "xdmp:eval(\"xquery version '1.0-ml'; " +
                "import module namespace sec = 'http://marklogic.com/xdmp/security' at '/MarkLogic/security.xqy'; " +
                "for \$username in (${usernamesStr}) where sec:user-exists(\$username) return sec:remove-user(\$username) " +
                "\", (), <options xmlns='xdmp:eval'><database>{xdmp:security-database()}</database></options>)";

        println "Removing users ${usernamesStr}"
        new XccHelper(xccUrl).executeXquery(xquery)
    }
}
