package com.marklogic.gradle.task.cpf

import org.gradle.api.tasks.TaskAction

class InstallMarkLogicPipelineTask extends CpfTask {

    String filename
    
    @TaskAction
    void installMarkLogicPipeline() {
        newCpfHelper().installMarkLogicPipeline(filename)
    }
}
