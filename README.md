marklogic-java
==============

This project is organized as follows:

- ml-java eases the use of the MarkLogic Java Client API, in particular with installing modules via the REST API.
- ml-junit provides support with writing JUnit tests, particularly ones that use Spring's test framework
- ml-gradle provides a Gradle plugin that installs a number of tasks to simplify using the MarkLogic REST API to install and configure MarkLogic applications
- sample-project is intended to show each of the 3 libraries above in action

All of these modules depend on MarkLogic 7 or higher and Java 1.7 or higher. 

The easiest way to get started with these modules is to create a new project with a build.gradle file that looks like the one at sample-project/build.gradle. A simple version appears below - it just declares repositories and dependencies so that ml-gradle can be used in the build file, and so that ml-java and ml-junit are available for application and test code, respectively. 

    buildscript {
      repositories {
        mavenCentral()
        maven {url "http://developer.marklogic.com/maven2/"}
        maven {url "http://rjrudin.github.io/marklogic-java/releases"}
      }
      
      dependencies {
        classpath "com.marklogic:ml-gradle:0.5"
      }
    }

    apply plugin: 'eclipse'
    apply plugin: 'java'
    apply plugin: 'ml-gradle'

    repositories {
      mavenCentral()
      maven {url "http://developer.marklogic.com/maven2/"}
      maven {url "http://rjrudin.github.io/marklogic-java/releases"}
    }

    dependencies {
      compile "com.marklogic:ml-java:0.5"
      testCompile "com.marklogic:ml-junit:0.5"
    }

    ext {
      mlUsername = "admin"
      mlPassword = "admin"
      
      mlAppConfig {
        name = "changeme"
      }
    }

Once you have a Gradle file like this (toss in a file named "build.gradle" and save it to an empty directory), just run "gradle mlInstallApp mlConfigureApp" (or "gradle mlin mlconf" - Gradle will expand those names for you). You'll then have a REST API server named "changeme" running on port 8100, an XDBC server named "changeme-xdbc" running on port 8101, and content and modules databases and forests that those servers point to. Check the Wiki pages on the right and the source code for additional tasks and features. 
