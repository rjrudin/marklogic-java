package com.marklogic.gradle.task.cpf

import org.gradle.api.tasks.TaskAction

class InstallSystemPipelineTask extends CpfTask {

    String filename
    
    @TaskAction
    void installMarkLogicPipeline() {
        newCpfHelper().installSystemPipeline(filename)
    }
}
