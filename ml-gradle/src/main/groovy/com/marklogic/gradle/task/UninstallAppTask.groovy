package com.marklogic.gradle.task

import org.gradle.api.tasks.TaskAction
import org.springframework.core.io.ClassPathResource
import org.springframework.util.FileCopyUtils

import com.marklogic.gradle.MarkLogicTask
import com.marklogic.gradle.xcc.XccHelper

class UninstallAppTask extends MarkLogicTask {

    String xccUrl

    @TaskAction
    void uninstallApp() {
        if (!xccUrl) {
            xccUrl = getDefaultXccUrl()
        }

        String xquery = new String(FileCopyUtils.copyToByteArray(new ClassPathResource("ml-gradle/uninstall-app.xqy").getInputStream()))
        xquery = xquery.replace("%%APP_NAME%%", getAppName())
        println "Uninstalling app with name " + getAppName()
        try {
            println new XccHelper(xccUrl).executeXquery(xquery)
        } catch (Exception e) {
            printn "Unable to uninstall app, cause: " + e.getMessage()
        }
    }
}
