package com.marklogic.gradle.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import com.marklogic.client.configurer.PropertiesConfigurationFilesManager;

class DeleteLastConfiguredTimestampsFileTask extends DefaultTask {

    String filePath = PropertiesConfigurationFilesManager.DEFAULT_FILE_PATH

    @TaskAction
    void deleteFile() {
        File f = new File(filePath)
        if (f.exists()) {
            println "Deleting " + f.getAbsolutePath() + "\n"
            f.delete()
        }
    }
}
