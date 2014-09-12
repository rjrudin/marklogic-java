package com.marklogic.gradle.task.manage

import groovyx.net.http.HttpResponseDecorator

import org.gradle.api.Project

import com.marklogic.gradle.MarkLogicTask
import com.marklogic.gradle.RestHelper

class AbstractManageTask extends MarkLogicTask {

    ManageConfig getManageConfig() {
        ManageConfig config = getProject().property("mlManageConfig")
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