package com.marklogic.gradle.task.manage

/**
 * Configuration data for talking to the Manage REST API.
 */
class ManageConfig {

	String host = "localhost"
    Integer port = 8002
	String username = "admin"
	String password = "admin"
    
    String contentDatabaseFilePath = "src/main/xqy/packages/content-database.xml"
    String httpServerFilePath = "src/main/xqy/packages/http-server.xml"
}
