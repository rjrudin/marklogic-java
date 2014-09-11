package com.marklogic.gradle

class AppConfig {

    String name = "sample-app"
    String host = "localhost"
    String username
    String password
    
    Integer restPort = 8100
    Integer xdbcPort = 8101
    Integer testRestPort
    Integer testXdbcPort
    
    String defaultConfigPath = "src/main/xqy"
    List<String> configPaths
    
    public AppConfig() {
        configPaths = new ArrayList<String>()
        configPaths.add(defaultConfigPath)
    }
}
