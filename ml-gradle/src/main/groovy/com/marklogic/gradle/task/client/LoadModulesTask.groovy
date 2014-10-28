package com.marklogic.gradle.task.client

import org.gradle.api.tasks.TaskAction

import com.marklogic.client.DatabaseClient
import com.marklogic.client.modulesloader.ModulesLoader
import com.marklogic.client.modulesloader.impl.DefaultModulesLoader;

class LoadModulesTask extends ClientTask {

    List<String> modulePaths
    String modulesLoaderClassName

    @TaskAction
    void loadModules() {
        DatabaseClient client = newClient()
        
        ModulesLoader loader = null
        if (modulesLoaderClassName) {
            loader = Class.forName(modulesLoaderClassName).newInstance()
        } else {
            loader = new DefaultModulesLoader()
        }
        
        List<String> directories = modulePaths != null ? modulePaths : getAppConfig().modulePaths
        println "Module paths: " + directories
        try {
            for (int i = 0; i < directories.size(); i++) {
                String dir = directories.get(i);
                println "Loading modules found at ${dir}"
                loader.loadModules(new File(dir), client)
            }

            println "Finished loading modules\n"
        } finally {
            client.release()
        }
    }
}
