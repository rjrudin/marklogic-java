package com.marklogic.gradle.task.cpf

import org.gradle.api.tasks.TaskAction

class CreateDomainConfigurationTask extends CpfTask {

    String restartUser
    String modulesDatabaseName
    String defaultDomainName
    String[] permissions

    @TaskAction
    void createDomainConfiguration() {
        def dbName = modulesDatabaseName ? modulesDatabaseName : getAppConfig().name + "-modules"
        newCpfHelper().createDomainConfiguration(restartUser, dbName, defaultDomainName, permissions)
    }
}
