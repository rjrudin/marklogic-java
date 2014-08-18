package com.marklogic.gradle.task.manage

import groovyx.net.http.HttpResponseDecorator

import org.gradle.api.DefaultTask
import org.gradle.api.Project

import com.marklogic.gradle.MarkLogicTask;
import com.marklogic.gradle.RestHelper

class AbstractManageTask extends MarkLogicTask {

    ManageConfig getManageConfig() {
        getProject().property("mlManageConfig")
    }

    String getManageUsername() {
        ManageConfig config = getManageConfig()
        if (config.getUsername() != null) {
            return config.getUsername()
        }
        return getMlUsername()
    }
    
    String getManagePassword() {
        ManageConfig config = getManageConfig()
        if (config.getPassword() != null) {
            return config.getPassword()
        }
        return getMlPassword()
    }

    RestHelper newRestHelper() {
        ManageConfig manageConfig = getProject().property("mlManageConfig")
        RestHelper h = new RestHelper()
        h.setUrl("http://" + manageConfig.getHost() + ":" + manageConfig.getPort())
        h.setUsername(getManageUsername())
        h.setPassword(getManagePassword())
        return h
    }

    HttpResponseDecorator invoke(Project project, String method, String path, String body, String requestContentType) {
        return newRestHelper().invoke(method, path, body, requestContentType)
    }

    HttpResponseDecorator invoke(Project project, String method, String path) {
        return newRestHelper().invoke(method, path)
    }
}