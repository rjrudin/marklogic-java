package com.marklogic.client.configurer.ml7;

import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.FailedRequestException;
import com.marklogic.client.admin.ExtensionLibrariesManager;
import com.marklogic.client.admin.ExtensionMetadata;
import com.marklogic.client.admin.QueryOptionsManager;
import com.marklogic.client.admin.ResourceExtensionsManager;
import com.marklogic.client.admin.ResourceExtensionsManager.MethodParameters;
import com.marklogic.client.admin.TransformExtensionsManager;
import com.marklogic.client.configurer.Asset;
import com.marklogic.client.configurer.ConfigurationFiles;
import com.marklogic.client.configurer.ConfigurationFilesFinder;
import com.marklogic.client.configurer.ConfigurationFilesManager;
import com.marklogic.client.configurer.DefaultConfigurationFilesFinder;
import com.marklogic.client.configurer.FilenameUtil;
import com.marklogic.client.configurer.PropertiesConfigurationFilesManager;
import com.marklogic.client.configurer.metadata.ExtensionMetadataAndParams;
import com.marklogic.client.configurer.metadata.ExtensionMetadataProvider;
import com.marklogic.client.configurer.metadata.XmlExtensionMetadataProvider;
import com.marklogic.client.io.FileHandle;
import com.marklogic.client.io.Format;
import com.marklogic.util.LoggingObject;

public class RestApiConfigurer extends LoggingObject {

    private DatabaseClient client;
    private ExtensionMetadataProvider extensionMetadataProvider;
    private ConfigurationFilesFinder configurationFilesFinder;
    private ConfigurationFilesManager configurationFilesManager;

    public RestApiConfigurer(DatabaseClient client) {
        this.client = client;
        this.extensionMetadataProvider = new XmlExtensionMetadataProvider();
        this.configurationFilesFinder = new DefaultConfigurationFilesFinder();
        this.configurationFilesManager = new PropertiesConfigurationFilesManager();
    }

    public Set<File> loadModules(File baseDir) {
        configurationFilesManager.initialize();

        ConfigurationFiles files = configurationFilesFinder.findConfigurationFiles(baseDir);

        Set<File> loadedModules = new HashSet<>();
        
        for (Asset asset : files.getAssets()) {
            File f = installAsset(asset);
            if (f != null) {
                loadedModules.add(f);
            }
        }

        for (File f : files.getOptions()) {
            f = installQueryOptions(f);
            if (f != null) {
                loadedModules.add(f);
            }
        }

        for (File f : files.getTransforms()) {
            ExtensionMetadataAndParams emap = extensionMetadataProvider.provideExtensionMetadataAndParams(f);
            f = installTransform(f, emap.metadata);
            if (f != null) {
                loadedModules.add(f);
            }
        }

        for (File f : files.getServices()) {
            ExtensionMetadataAndParams emap = extensionMetadataProvider.provideExtensionMetadataAndParams(f);
            f = installResource(f, emap.metadata, emap.methods.toArray(new MethodParameters[] {}));
            if (f != null) {
                loadedModules.add(f);
            }
        }
        
        return loadedModules;
    }

    public File installAsset(Asset asset) {
        File file = asset.getFile();
        if (!configurationFilesManager.hasFileBeenModifiedSinceLastInstalled(file)) {
            return null;
        }
        ExtensionLibrariesManager libMgr = client.newServerConfigManager().newExtensionLibrariesManager();
        Format format = determineFormat(file);
        FileHandle h = new FileHandle(file);
        String path = "/ext" + asset.getPath();
        logger.info(String.format("Installing asset at path %s from file %s", path, file.getAbsolutePath()));
        try {
            libMgr.write(path, h.withFormat(format));
        } catch (FailedRequestException fre) {
            logger.warn("Caught exception, retrying as binary file; exception message: " + fre.getMessage());
            libMgr.write(path, h.withFormat(Format.BINARY));
        }
        configurationFilesManager.saveLastInstalledTimestamp(file, new Date());
        return file;
    }

    /**
     * TODO Need something pluggable here - probably should delegate this to a separate object so that a client could
     * easily provide a different implementation in case the assumptions below aren't correct.
     * 
     * @param file
     * @return
     */
    protected Format determineFormat(File file) {
        String name = file.getName();
        if (FilenameUtil.isXslFile(name) || name.endsWith(".xml") || name.endsWith(".html")) {
            return Format.XML;
        } else if (name.endsWith(".swf") || name.endsWith(".jpeg") || name.endsWith(".jpg") || name.endsWith(".png")
                || name.endsWith(".gif") || name.endsWith(".svg") || name.endsWith(".ttf") || name.endsWith(".eot")
                || name.endsWith(".woff") || name.endsWith(".cur")) {
            return Format.BINARY;
        }
        return Format.TEXT;
    }

    public File installResource(File file, ExtensionMetadata metadata, MethodParameters... methodParams) {
        if (!configurationFilesManager.hasFileBeenModifiedSinceLastInstalled(file)) {
            return null;
        }
        ResourceExtensionsManager extMgr = client.newServerConfigManager().newResourceExtensionsManager();
        String resourceName = getExtensionNameFromFile(file);
        if (metadata.getTitle() == null) {
            metadata.setTitle(resourceName + " resource extension");
        }
        logger.info(String.format("Installing %s resource extension from file %s", resourceName, file));
        try {
            extMgr.writeServices(resourceName, new FileHandle(file), metadata, methodParams);
            configurationFilesManager.saveLastInstalledTimestamp(file, new Date());
            return file;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public File installTransform(File file, ExtensionMetadata metadata) {
        if (!configurationFilesManager.hasFileBeenModifiedSinceLastInstalled(file)) {
            return null;
        }
        TransformExtensionsManager mgr = client.newServerConfigManager().newTransformExtensionsManager();
        String transformName = getExtensionNameFromFile(file);
        logger.info(String.format("Installing %s transform from file %s", transformName, file));
        try {
            if (FilenameUtil.isXslFile(file.getName())) {
                mgr.writeXSLTransform(transformName, new FileHandle(file), metadata);
            } else {
                mgr.writeXQueryTransform(transformName, new FileHandle(file), metadata);
            }
            configurationFilesManager.saveLastInstalledTimestamp(file, new Date());
            return file;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public File installQueryOptions(File f) {
        if (!configurationFilesManager.hasFileBeenModifiedSinceLastInstalled(f)) {
            return null;
        }
        String name = getExtensionNameFromFile(f);
        logger.info(String.format("Installing %s query options from file %s", name, f.getName()));
        QueryOptionsManager mgr = client.newServerConfigManager().newQueryOptionsManager();
        mgr.writeOptions(name, new FileHandle(f));
        configurationFilesManager.saveLastInstalledTimestamp(f, new Date());
        return f;
    }

    protected String getExtensionNameFromFile(File file) {
        String name = file.getName();
        int pos = name.lastIndexOf('.');
        if (pos < 0)
            return name;
        return name.substring(0, pos);
    }

    public void setExtensionMetadataProvider(ExtensionMetadataProvider extensionMetadataProvider) {
        this.extensionMetadataProvider = extensionMetadataProvider;
    }

    public void setConfigurationFilesFinder(ConfigurationFilesFinder extensionFilesFinder) {
        this.configurationFilesFinder = extensionFilesFinder;
    }

    public void setConfigurationFilesManager(ConfigurationFilesManager configurationFilesManager) {
        this.configurationFilesManager = configurationFilesManager;
    }
}
