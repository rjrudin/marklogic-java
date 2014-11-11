package com.marklogic.gradle.task.cpf

import org.gradle.api.tasks.TaskAction

class RemoveDomainTask extends CpfTask {

    String domainName

    @TaskAction
    void removeDomain() {
        try {
            newCpfHelper().removeDomain(domainName)
            println "Removed CPF domain: " + domainName + "\n"
        } catch (Exception ex) {
            println "Could not remove domain, perhaps because it does not exist; cause: " + ex.getMessage() + "\n"
        }
    }
}
