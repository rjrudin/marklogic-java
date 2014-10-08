package com.marklogic.gradle.task.client

import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.TaskAction;

import com.marklogic.gradle.AppConfig;

class WatchTask extends JavaExec {

    @TaskAction
    @Override
    public void exec() {
        setMain("com.marklogic.client.configurer.ml7.ModulesWatcher")
        setClasspath(getProject().sourceSets.main.runtimeClasspath)

        AppConfig config = getProject().property("mlAppConfig")
        def username = config.getUsername() ? config.getUsername() : getProject().property("mlUsername")
        def password = config.getPassword() ? config.getPassword() : getProject().property("mlPassword")
        setArgs([
            config.getConfigPaths().join(","),
            config.getHost(),
            config.getRestPort(),
            username,
            password
        ])

        super.exec()
    }
}
