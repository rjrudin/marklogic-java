package com.marklogic.gradle.task.client

import org.gradle.api.tasks.TaskAction

import com.marklogic.client.DatabaseClient
import com.marklogic.client.configurer.ml7.RestApiConfigurer

class ConfigureAppTask extends ClientTask {

    @TaskAction
    void configureApp() {
        DatabaseClient client = newClient()
        List<String> directories = getAppConfig().configPaths
        try {
            for (int i = 0; i < directories.size(); i++) {
                String dir = directories.get(i);
                println "Installing configuration found at ${dir}"
                new RestApiConfigurer(client).configure(new File(dir))
            }
        } finally {
            client.release()
        }
    }
}
