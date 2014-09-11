package com.marklogic.gradle.task.security

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import com.marklogic.gradle.MarkLogicTask;
import com.marklogic.gradle.xcc.XccHelper


class CreateUserTask extends MarkLogicTask {

    String xccUrl
    String username
    String description
    String password
    String[] roleNames
    String[] collections
    
    @TaskAction
    void executeXquery() {
        String roleNamesStr = "'" + roleNames.join("', '") + "'"
        String collectionsStr = collections ? "'" + collections.join("', '") + "'" : ""
        
        String xquery = "xdmp:eval(\"xquery version '1.0-ml'; " +
                "import module namespace sec = 'http://marklogic.com/xdmp/security' at '/MarkLogic/security.xqy'; " +
                "if (sec:user-exists('${username}')) then () else " + 
                "sec:create-user('${username}', '${description}', '${password}', (${roleNamesStr}), (), (${collectionsStr})) " +
                "\", (), <options xmlns='xdmp:eval'><database>{xdmp:security-database()}</database></options>)";

        println "Creating user ${username}"
        
        if (!xccUrl) {
            xccUrl = getDefaultXccUrl()
        }
        
        new XccHelper(xccUrl).executeXquery(xquery)
    }
}
