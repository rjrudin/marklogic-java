package com.marklogic.client.configurer;

import java.io.File;

public interface ConfigurationFilesFinder {

    public ConfigurationFiles findConfigurationFiles(File dir);
}
