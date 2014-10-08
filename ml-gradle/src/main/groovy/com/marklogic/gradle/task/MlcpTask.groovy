package com.marklogic.gradle.task

import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.TaskAction

/**
 * Provides parameters for some, but not all, mlcp arguments. Arguments that aren't supported can be passed in
 * via JavaExec's "args" property. The main benefit of using this class is that it assumes usage of the connection
 * properties found in the mlAppConfig project property.
 */
class MlcpTask extends JavaExec {

    String command
    String input_file_path
    String input_file_type
    String input_compressed
    String document_type
    String output_collections
    String delimited_root_name
    String delimited_uri_id
    String namespace
    String options_file
    String output_uri_prefix
    String transform_module
    String transform_namespace
    String thread_count
    
    @TaskAction
    @Override
    public void exec() {
        setMain("com.marklogic.contentpump.ContentPump")
        com.marklogic.gradle.AppConfig config = getProject().property("mlAppConfig")

        List<String> newArgs = new ArrayList<>()
        newArgs.add(command)
        newArgs.add("-host")
        newArgs.add(config.getHost())
        newArgs.add("-port")
        newArgs.add(config.getXdbcPort())
        newArgs.add("-username")
        newArgs.add(config.getUsername())

        if (input_file_path) {
            newArgs.add("-input_file_path")
            newArgs.add(input_file_path)
        }
        if (input_file_type) {
            newArgs.add("-input_file_type")
            newArgs.add(input_file_type)
        }
        if (input_compressed) {
            newArgs.add("-input_compressed")
            newArgs.add(input_compressed)
        }
        if (document_type) {
            newArgs.add("-document_type")
            newArgs.add(document_type)
        }
        if (output_collections) {
            newArgs.add("-output_collections")
            newArgs.add(output_collections)
        }
        if (delimited_root_name) {
            newArgs.add("-delimited_root_name")
            newArgs.add(delimited_root_name)
        }
        if (delimited_uri_id) {
            newArgs.add("-delimited_uri_id")
            newArgs.add(delimited_uri_id)
        }
        if (namespace) {
            newArgs.add("-namespace")
            newArgs.add(namespace)
        }
        if (options_file) {
            newArgs.add("-options_file")
            newArgs.add(options_file)
        }
        if (output_uri_prefix) {
            newArgs.add("-output_uri_prefix")
            newArgs.add(output_uri_prefix)
        }
        if (transform_module) {
            newArgs.add("-transform_module")
            newArgs.add(transform_module)
        }
        if (transform_namespace) {
            newArgs.add("-transform_namespace")
            newArgs.add(transform_namespace)
        }
        if (thread_count) {
            newArgs.add("-thread_count")
            newArgs.add(thread_count)
        }
        
        newArgs.addAll(getArgs())
        
        println "mlcp arguments, excluding password: " + newArgs
        
        newArgs.add("-password")
        newArgs.add(config.getPassword())
        
        setArgs(newArgs)

        super.exec()
    }
}
