marklogic-java
==============

This project is organized as follows:

- ml-java eases the use of the MarkLogic Java Client API, in particular with installing modules via the REST API.
- ml-junit provides support with writing JUnit tests, particularly ones that use Spring's test framework
- ml-gradle provides a Gradle plugin that installs a number of tasks to simplify using the MarkLogic REST API to install and configure MarkLogic applications
- sample-project is intended to show each of the 3 libraries above in action

The easiest way to get started with these modules is to create a new project with a build.gradle file that looks like the one at sample-project/build.gradle. A simple version appears below - it just declares repositories and dependencies so that ml-gradle can be used in the build file, and so that ml-java and ml-junit are available for application and test code, respectively. 

    buildscript {
      repositories {
        mavenCentral()
        maven {url "https://developer.marklogic.com/maven2/"}
        maven {url "http://rjrudin.github.io/marklogic-java/releases"}
      }
      
      dependencies {
        classpath group:"com.marklogic", name:"ml-gradle", version:"0.2"
      }
    }

    apply plugin: 'eclipse'
    apply plugin: 'java'
    apply plugin: 'ml-gradle'

    repositories {
      mavenCentral()
      maven {url "https://developer.marklogic.com/maven2/"}
      maven {url "http://rjrudin.github.io/marklogic-java/releases"}
    }

    dependencies {
      compile group:"com.marklogic", name:"ml-java", version:"0.2"
      testCompile group:"com.marklogic", name:"ml-junit", version:"0.2"
    }

    ext {
      mlUsername = "admin"
      mlPassword = "admin"
      
      mlAppConfig {
        name = "sample-project"
      }
    }
