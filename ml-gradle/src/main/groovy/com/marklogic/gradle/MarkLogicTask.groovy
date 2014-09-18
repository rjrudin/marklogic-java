package com.marklogic.gradle

import org.gradle.api.DefaultTask;

class MarkLogicTask extends DefaultTask {

    AppConfig getAppConfig() {
        getProject().property("mlAppConfig")
    }
    
    String getDefaultXccUrl() {
        AppConfig config = getAppConfig()
        "xcc://${config.getUsername()}:${config.getPassword()}@${config.getHost()}:${config.getXdbcPort()}"
    }
    
    boolean isTestPortSet() {
        getAppConfig().getTestRestPort() != null
    }
}
