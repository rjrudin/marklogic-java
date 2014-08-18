package com.marklogic.gradle.task.manage

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction
import org.springframework.core.io.ClassPathResource
import org.springframework.util.FileCopyUtils

import com.marklogic.manage.pkg.databases.DatabasePackageMerger;

/**
 * Use the initial package in src/main/resources, can allow for it to be overridden in the future.
 */
class MergeDatabasePackagesTask extends AbstractManageTask {
    
    List<String> mergePackageFilePaths
    String outputPath
    
    @TaskAction
    void mergeDatabasePackages() {
        if (!mergePackageFilePaths) {
            println "No mergePackageFilePaths specified, will not produce a new database package file"
            return 
        }
        
        if (!outputPath) {
            outputPath = getManageConfig().getContentDatabaseFilePath()
        }
        
        File outputFile = new File(outputPath)
        if (outputFile.exists()) {
            println "Deleting existing output file: " + outputFile.getAbsolutePath()
            outputFile.delete()
        }
        
        println "Merging database package files: " + mergePackageFilePaths
        String xml = new DatabasePackageMerger().mergeDatabasePackages(mergePackageFilePaths)
        println "Writing merged database package to " + outputFile.getAbsolutePath()
        File dir = outputFile.getParentFile()
        if (dir != null) {
            dir.mkdirs()
        }
        FileCopyUtils.copy(xml,  new FileWriter(outputFile))
    }
}
