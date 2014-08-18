package com.marklogic.client.configurer.metadata;

import java.io.File;

public interface ExtensionMetadataProvider {

    public ExtensionMetadataAndParams provideExtensionMetadataAndParams(File resourceFile);
}
