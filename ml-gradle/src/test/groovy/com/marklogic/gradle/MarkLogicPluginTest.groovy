package com.marklogic.gradle

import org.junit.Test
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.api.Project
import org.gradle.api.DefaultTask
import static org.junit.Assert.*

class GreetingPluginTest {
    @Test
    public void happyPath() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'ml-gradle'
        
        //assertTrue(project.tasks.mlHello instanceof DefaultTask)
    }
}