package com.marklogic.gradle

import org.gradle.api.DefaultTask;

class MarkLogicTask extends DefaultTask {

    AppConfig getAppConfig() {
        getProject().property("mlAppConfig")
    }
    
    String getDefaultXccUrl() {
        getAppConfig().getXccUrl()
    }
    
    boolean isTestPortSet() {
        getAppConfig().getTestRestPort() != null
    }
}
