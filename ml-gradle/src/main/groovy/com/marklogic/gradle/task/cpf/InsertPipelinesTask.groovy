package com.marklogic.gradle.task.cpf

import org.gradle.api.tasks.TaskAction

class InsertPipelinesTask extends CpfTask {

    String[] filePaths

    @TaskAction
    void insertPipelines() {
        def h = newCpfHelper()
        for (String path : filePaths) {
            def xml = new File(path).text
            // This is hacky; we need to remove double quotes so they don't mess up the xdmp:eval call
            // TODO Perhaps xdmp:invoke-function is the better tool?
            xml = xml.replace('"', "'")
            h.evaluateAgainstTriggersDatabase("p:insert(" + xml + ")")
            println "Inserted pipeline from path: " + path
        }
        println "Finished inserting pipelines\n"
    }
}
