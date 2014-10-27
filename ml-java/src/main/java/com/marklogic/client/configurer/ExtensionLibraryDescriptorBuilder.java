package com.marklogic.client.configurer;

import com.marklogic.client.admin.ExtensionLibraryDescriptor;

/**
 * Main purpose of this interface is to provide an extension point in RestApiModulesLoader for
 * setting document permissions for a particular asset.
 */
public interface ExtensionLibraryDescriptorBuilder {

    ExtensionLibraryDescriptor buildDescriptor(Asset asset);
}
