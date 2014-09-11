package com.marklogic.gradle.task.client

import org.gradle.api.tasks.TaskAction

import com.marklogic.client.DatabaseClient
import com.marklogic.client.configurer.ml7.RestApiConfigurer

class ConfigureAppTask extends ClientTask {

    @TaskAction
    void configureApp() {
        DatabaseClient client = newClient()
        RestApiConfigurer configurer = new RestApiConfigurer(client)
        List<String> directories = getAppConfig().configPaths
        println "Configuration paths: " + directories
        try {
            for (int i = 0; i < directories.size(); i++) {
                String dir = directories.get(i);
                println "Installing configuration found at ${dir}"
                configurer.configure(new File(dir))
            }
        } finally {
            client.release()
        }
    }
}
