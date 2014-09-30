package com.marklogic.gradle.task.manage

import groovyx.net.http.HttpResponseDecorator

import org.gradle.api.Project

import com.marklogic.gradle.MarkLogicTask
import com.marklogic.gradle.RestHelper

class ManageTask extends MarkLogicTask {

    ManageConfig getManageConfig() {
        getProject().property("mlManageConfig")
    }

    RestHelper newRestHelper() {
        ManageConfig config = getManageConfig()
        RestHelper h = new RestHelper()
        h.setUrl("http://" + config.getHost() + ":" + config.getPort())
        h.setUsername(config.getUsername())
        h.setPassword(config.getPassword())
        return h
    }

    HttpResponseDecorator invoke(Project project, String method, String path, String body, String requestContentType) {
        return newRestHelper().invoke(method, path, body, requestContentType)
    }

    HttpResponseDecorator invoke(Project project, String method, String path) {
        return newRestHelper().invoke(method, path)
    }
}