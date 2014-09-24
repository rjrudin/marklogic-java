package com.marklogic.gradle.task.client

import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.TaskAction
import org.gradle.api.internal.project.DefaultAntBuilder

/**
 * Purpose of this task is to unzip each restApi dependency to the build directory and then register the path of each
 * unzipped directory in mlConfigPaths. 
 */
class PrepareRestApiDependenciesTask extends ClientTask {

    @TaskAction
    void prepareRestApiDependencies() {
        String configurationName = "mlRestApi"
        if (getProject().configurations.find {it.name == configurationName}) {
            Configuration config = getProject().getConfigurations().getAt(configurationName)
            if (config.files) {
                println "Found " + configurationName + " configuration, will unzip all of its dependencies to build/mlRestApi"
                
                def buildDir = new File("build/mlRestApi")
                buildDir.delete()
                buildDir.mkdirs()

                // Constructing a DefaultAntBuilder seems to avoid Xerces-related classpath issues
                def ant = new DefaultAntBuilder(getProject())
                for (f in config.files) {
                    println "Unzipping file: " + f.getAbsolutePath()
                    ant.unzip(src: f, dest: buildDir, overwrite: "true")
                }

                List<String> configPaths = getAppConfig().configPaths
                List<String> newConfigPaths = new ArrayList<String>()
                
                for (dir in buildDir.listFiles()) {
                    if (dir.isDirectory()) {
                        newConfigPaths.add(dir.getAbsolutePath())
                    }
                }
                
                // The config paths of the dependencies should be before the original config paths 
                newConfigPaths.addAll(configPaths)
                getAppConfig().setConfigPaths(newConfigPaths)
                
                println "Finished unzipping mlRestApi dependencies; will now include modules at " + getAppConfig().configPaths + "\n"
            } else {
            println "No mlRestApi dependencies found, so no preparation of dependencies required\n"
            }
        } else {
            println "No mlRestApi configuration found, so no preparation of mlRestApi dependencies required\n"
        }
    }
}
