package com.marklogic.gradle.task.cpf

import org.gradle.api.tasks.TaskAction

class CreateDomainTask extends CpfTask {

    String domainName
    String domainDescription

    String scope
    String scopeUri
    String scopeDepth = "/"

    String modulesDatabaseName

    // assume modules database name and root of "/"
    String[] pipelineNames
    String[] permissions

    @TaskAction
    void createDomain() {
        def dbName = modulesDatabaseName ? modulesDatabaseName : getAppConfig().name + "-modules"
        newCpfHelper().createDomain(domainName, domainDescription, scope, scopeUri, scopeDepth, dbName, pipelineNames, permissions)
    }
}
