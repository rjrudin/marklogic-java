package com.marklogic.gradle

import org.gradle.api.DefaultTask;

class MarkLogicTask extends DefaultTask {

    String getAppName() {
        getAppConfig().getName()
    }

    AppConfig getAppConfig() {
        getProject().property("mlAppConfig")
    }
    
    String getDefaultXccUrl() {
        "xcc://${getClientUsername()}:${getClientPassword()}@${getAppConfig().getHost()}:${getAppConfig().getXdbcPort()}"
    }
    
    boolean isTestPortSet() {
        getAppConfig().getTestRestPort() != null
    }
    
    /**
     * mlUsername is considered a "default" username to be used for both the manage and client configuration
     * when no username is specified for those.
     * 
     * @return
     */
    String getMlUsername() {
        getProject().property("mlUsername")
    }
    
    /**
     * mlPassword is considered a "default" username to be used for both the manage and client configuration
     * when no password is specified for those.
     * 
     * @return
     */
    String getMlPassword() {
        getProject().property("mlPassword")
    }
    
    String getClientUsername() {
        AppConfig config = getAppConfig()
        if (config.getUsername() != null) {
            return config.getUsername()
        }
        return getMlUsername()
    }
    
    String getClientPassword() {
        AppConfig config = getAppConfig()
        if (config.getPassword() != null) {
            return config.getPassword()
        }
        return getMlPassword()
    }
}
