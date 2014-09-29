package com.marklogic.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

import com.marklogic.gradle.task.DeleteLastConfiguredTimestampsFileTask
import com.marklogic.gradle.task.UninstallAppTask
import com.marklogic.gradle.task.client.LoadModulesTask
import com.marklogic.gradle.task.client.PrepareRestApiDependenciesTask
import com.marklogic.gradle.task.client.WatchTask
import com.marklogic.gradle.task.client.config.CreateTransformTask;
import com.marklogic.gradle.task.client.service.CreateResourceTask
import com.marklogic.gradle.task.database.ClearModulesTask;
import com.marklogic.gradle.task.manage.ConfigureBitemporalTask
import com.marklogic.gradle.task.manage.InstallAppTask
import com.marklogic.gradle.task.manage.ManageConfig
import com.marklogic.gradle.task.manage.MergeDatabasePackagesTask
import com.marklogic.gradle.task.manage.MergeHttpServerPackagesTask
import com.marklogic.gradle.task.manage.UpdateDatabaseTask
import com.marklogic.gradle.task.manage.UpdateHttpServerTask

class MarkLogicPlugin implements Plugin<Project> {

    void apply(Project project) {
        initializeAppConfig(project)
        initializeManageConfig(project)

        project.getConfigurations().create("mlRestApi")

        String group = "MarkLogic"
        
        project.task("mlDeleteLastConfigured", type: DeleteLastConfiguredTimestampsFileTask, group: group)
        project.task("mlUninstallApp", type: UninstallAppTask, group: group)
        project.task("mlClearModules", type: ClearModulesTask, group: group, dependsOn: "mlDeleteLastConfigured")

        project.task("mlPrepareRestApiDependencies", type: PrepareRestApiDependenciesTask, group: group, dependsOn: project.configurations["mlRestApi"])

        project.task("mlMergeDatabasePackages", type: MergeDatabasePackagesTask, group: group, dependsOn:"mlPrepareRestApiDependencies")
        project.task("mlMergeHttpServerPackages", type: MergeHttpServerPackagesTask, group: group)

        project.task("mlInstallApp", type: InstallAppTask, group: group, dependsOn: [
            "mlMergeDatabasePackages",
            "mlMergeHttpServerPackages"
        ])

        project.task("mlLoadModules", type: LoadModulesTask, group: group, dependsOn: "mlPrepareRestApiDependencies")
        
        project.task("mlDeploy", group: group, dependsOn: [
            "mlDeleteLastConfigured",
            "mlInstallApp",
            "mlLoadModules"
        ])

        project.task("mlReloadModules", group: group, dependsOn: [
            "mlClearModules",
            "mlLoadModules"
        ])
        
        project.task("mlUpdateContentDatabase", type: UpdateDatabaseTask, group: group, dependsOn: "mlMergeDatabasePackages")
        project.task("mlUpdateHttpServers", type: UpdateHttpServerTask, group: group, dependsOn: "mlMergeHttpServerPackages")

        project.task("mlCreateResource", type: CreateResourceTask, group: group)
        project.task("mlCreateTransform", type: CreateTransformTask, group: group)
        
        project.task("mlConfigureBitemporal", type: ConfigureBitemporalTask, group: group)

        project.task("mlWatch", type: WatchTask, group: group)
    }

    void initializeAppConfig(Project project) {
        AppConfig appConfig = new AppConfig()

        if (project.hasProperty("mlAppName")) {
            def name = project.property("mlAppName")
            println "App name: " + name
            appConfig.setName(name)
        }
        if (project.hasProperty("mlHost")) {
            def host = project.property("mlHost")
            println "App host: " + host
            appConfig.setHost(host)
        }
        if (project.hasProperty("mlUsername")) {
            def username = project.property("mlUsername")
            println "App username: " + username
            appConfig.setUsername(username)
        }
        if (project.hasProperty("mlPassword")) {
            appConfig.setPassword(project.property("mlPassword"))
        }

        if (project.hasProperty("mlRestPort")) {
            def port = project.property("mlRestPort")
            println "App REST port: " + port
            appConfig.setRestPort(Integer.parseInt(port))
        }
        if (project.hasProperty("mlTestRestPort")) {
            def port = project.property("mlTestRestPort")
            println "App test REST port: " + port
            appConfig.setTestRestPort(Integer.parseInt(port))
        }

        if (project.hasProperty("mlXdbcPort")) {
            def port = project.property("mlXdbcPort")
            println "App XDBC port: " + port
            appConfig.setXdbcPort(Integer.parseInt(port))
        }
        if (project.hasProperty("mlTestXdbcPort")) {
            def port = project.property("mlTestXdbcPort")
            println "App test XDBC port: " + port
            appConfig.setTestXdbcPort(Integer.parseInt(port))
        }

        project.extensions.add("mlAppConfig", appConfig)
    }

    void initializeManageConfig(Project project) {
        ManageConfig manageConfig = new ManageConfig()
        if (project.hasProperty("mlHost")) {
            def host = project.property("mlHost")
            println "Manage app host: " + host
            manageConfig.setHost(host)
        }
        if (project.hasProperty("mlUsername")) {
            def username = project.property("mlUsername")
            println "Manage app username: " + username
            manageConfig.setUsername(username)
        }
        if (project.hasProperty("mlPassword")) {
            manageConfig.setPassword(project.property("mlPassword"))
        }
        project.extensions.add("mlManageConfig", manageConfig)
    }
}
