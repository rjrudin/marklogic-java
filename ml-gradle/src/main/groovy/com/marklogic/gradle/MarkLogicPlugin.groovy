package com.marklogic.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task;

import com.marklogic.gradle.task.DeleteLastInstalledTimestampsFileTask
import com.marklogic.gradle.task.UninstallAppTask
import com.marklogic.gradle.task.client.ConfigureAppTask
import com.marklogic.gradle.task.client.PrepareRestApiDependenciesTask;
import com.marklogic.gradle.task.client.service.CreateResourceTask;
import com.marklogic.gradle.task.manage.InstallAppTask
import com.marklogic.gradle.task.manage.ManageConfig
import com.marklogic.gradle.task.manage.MergeDatabasePackagesTask
import com.marklogic.gradle.task.manage.MergeHttpServerPackagesTask
import com.marklogic.gradle.task.manage.UpdateDatabaseTask
import com.marklogic.gradle.task.manage.UpdateHttpServerTask

class MarkLogicPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.extensions.create("mlManageConfig", ManageConfig)
        project.extensions.create("mlAppConfig", AppConfig)

        project.getConfigurations().create("mlRestApi")

        String group = "MarkLogic"
        project.task("mlDeleteLastInstalled", type: DeleteLastInstalledTimestampsFileTask, group: group)
        project.task("mlUninstallApp", type: UninstallAppTask, group: group)

        project.task("mlPrepareRestApiDependencies", type: PrepareRestApiDependenciesTask, group: group, dependsOn: project.configurations["mlRestApi"])

        project.task("mlMergeDatabasePackages", type: MergeDatabasePackagesTask, group: group, dependsOn:"mlPrepareRestApiDependencies")
        project.task("mlMergeHttpServerPackages", type: MergeHttpServerPackagesTask, group: group)

        project.task("mlInstallApp", type: InstallAppTask, group: group, dependsOn: [
            "mlMergeDatabasePackages",
            "mlMergeHttpServerPackages"
        ])

        project.task("mlUpdateContentDatabase", type: UpdateDatabaseTask, group: group, dependsOn: "mlMergeDatabasePackages")
        project.task("mlUpdateHttpServers", type: UpdateHttpServerTask, group: group, dependsOn: "mlMergeHttpServerPackages")


        project.task("mlConfigureApp", type: ConfigureAppTask, group: group, dependsOn: "mlPrepareRestApiDependencies")

        project.task("mlCreateResource", type: CreateResourceTask, group: group)
    }
}
