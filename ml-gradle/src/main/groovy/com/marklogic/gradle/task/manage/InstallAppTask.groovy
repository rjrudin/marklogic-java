package com.marklogic.gradle.task.manage

import org.gradle.api.tasks.TaskAction

import com.marklogic.gradle.AppConfig
import com.marklogic.gradle.RestHelper

class InstallAppTask extends AbstractManageTask {

    String groupName = "Default"
    String triggersDatabaseFilePath = "src/main/xqy/packages/triggers-database.xml"
    String xdbcServerFilename = "src/main/xqy/packages/xdbc-server.xml"

    String format = "json"

    @TaskAction
    void installApp() {
        AppConfig appConfig = getAppConfig()
        String appName = getAppName()
        String packageName = appName + "-package"
        String restServerName = appName

        RestHelper rh = newRestHelper()

        rh.deletePackage(packageName)
        rh.createPackage(packageName, format)

        boolean installPackage = false;
        boolean installTestResources = isTestPortSet();

        if (new File(triggersDatabaseFilePath).exists()) {
            String triggersDatabaseName = appName + "-triggers"
            rh.addDatabase(packageName, triggersDatabaseName, triggersDatabaseFilePath, format)
            installPackage = true
        }

        DatabasePackageManager mgr = new DatabasePackageManager(appName)
        String contentDatabaseFilePath = getManageConfig().getContentDatabaseFilePath()
        if (new File(contentDatabaseFilePath).exists()) {
            println "Installing databases based on content database package at " + contentDatabaseFilePath
            mgr.install(rh, contentDatabaseFilePath, installTestResources, format)
            installPackage = true
        } else {
            println "No content database package file found, so not installing a content database"
        }

        if (installPackage) {
            rh.installPackage(packageName, format)
        }

        rh.createRestApi(restServerName, mgr.getContentDatabaseName(), appConfig.getRestPort(), "")
        if (installTestResources) {
            String assumedModulesDatabase = appName + "-modules"
            rh.createRestApi(restServerName + "-test", mgr.getTestContentDatabaseName(), appConfig.getTestRestPort(), assumedModulesDatabase)
        }

        rh.addXdbcServer(packageName, groupName, appName, mgr.getContentDatabaseName(), appConfig.getXdbcPort(), xdbcServerFilename)
        if (installTestResources) {
            rh.addXdbcServer(packageName, groupName, appName, mgr.getTestContentDatabaseName(), appConfig.getTestXdbcPort(), xdbcServerFilename)
        }

        rh.installPackage(packageName, format)
        println "Successfully completed setup of application ${appName}"
    }
}
