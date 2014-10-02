package com.marklogic.gradle.task.client

import org.gradle.api.tasks.TaskAction

import com.marklogic.client.DatabaseClient
import com.marklogic.client.configurer.ml7.RestApiConfigurer

class LoadModulesTask extends ClientTask {

    @TaskAction
    void loadModules() {
        DatabaseClient client = newClient()
        RestApiConfigurer configurer = new RestApiConfigurer(client)
        List<String> directories = getAppConfig().configPaths
        println "Configuration paths: " + directories
        try {
            for (int i = 0; i < directories.size(); i++) {
                String dir = directories.get(i);
                println "Loading modules found at ${dir}"
                configurer.loadModules(new File(dir))
            }
            
            println "Finished loading modules\n"
        } finally {
            client.release()
        }
    }
}
