package com.marklogic.gradle

import org.gradle.api.DefaultTask;

class MarkLogicTask extends DefaultTask {

    String getAppName() {
        getAppConfig().getName()
    }

    AppConfig getAppConfig() {
        AppConfig config = getProject().property("mlAppConfig")
        if (getMlHost()) {
            config.setHost(getMlHost())
        }
        if (getMlUsername()) {
            config.setUsername(getMlUsername())
        }
        if (getMlPassword()) {
            config.setPassword(getMlPassword())
        }
        return config
    }
    
    String getDefaultXccUrl() {
        AppConfig config = getAppConfig()
        "xcc://${config.getUsername()}:${config.getPassword()}@${config.getHost()}:${config.getXdbcPort()}"
    }
    
    boolean isTestPortSet() {
        getAppConfig().getTestRestPort() != null
    }
    
    String getMlUsername() {
        getProject().property("mlUsername")
    }
    
    String getMlPassword() {
        getProject().property("mlPassword")
    }
    
    String getMlHost() {
        getProject().property("mlHost")
    }
}
