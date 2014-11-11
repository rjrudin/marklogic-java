package com.marklogic.gradle.task.cpf

class InstallAlertingPipelineTask extends InstallMarkLogicPipelineTask {

    public InstallAlertingPipelineTask() {
        setFilename("Installer/alert/alerting-pipeline.xml")
    }
}
