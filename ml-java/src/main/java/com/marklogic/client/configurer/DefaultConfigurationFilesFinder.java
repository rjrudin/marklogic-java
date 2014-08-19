package com.marklogic.client.configurer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class DefaultConfigurationFilesFinder implements ConfigurationFilesFinder {

    private FilenameFilter assetFilenameFilter = new AssetFilenameFilter();

    @Override
    public ConfigurationFiles findConfigurationFiles(File baseDir) {
        ConfigurationFiles files = new ConfigurationFiles();
        addServices(files, baseDir);
        addAssets(files, baseDir);
        addOptions(files, baseDir);
        addTransforms(files, baseDir);
        return files;
    }

    protected void addServices(ConfigurationFiles files, File baseDir) {
        File servicesBaseDir = new File(baseDir, "services");
        List<File> services = new ArrayList<>();
        if (servicesBaseDir.exists()) {
            for (File f : servicesBaseDir.listFiles()) {
                if (f.getName().endsWith(".xqy")) {
                    services.add(f);
                }
            }
        }
        files.setServices(services);
    }

    protected void addAssets(ConfigurationFiles files, File baseDir) {
        File assetsBaseDir = new File(baseDir, "ext");
        List<Asset> assets = new ArrayList<>();
        addAssetFiles(assetsBaseDir, "", assets);
        files.setAssets(assets);
    }

    protected void addAssetFiles(File dir, String pathRelativeToAssetsDir, List<Asset> assets) {
        if (dir.exists()) {
            for (File f : dir.listFiles(getAssetFilenameFilter())) {
                String name = f.getName();
                if (f.isDirectory()) {
                    addAssetFiles(f, pathRelativeToAssetsDir + "/" + name, assets);
                } else if (f.isFile()) {
                    assets.add(new Asset(f, pathRelativeToAssetsDir + "/" + name));
                }
            }
        }
    }

    /**
     * Can be overridden via subclass to provide custom behavior.
     * 
     * @return
     */
    protected FilenameFilter getAssetFilenameFilter() {
        return assetFilenameFilter;
    }

    protected void addOptions(ConfigurationFiles files, File baseDir) {
        File queryOptionsBaseDir = new File(baseDir, "options");
        List<File> queryOptions = new ArrayList<>();
        if (queryOptionsBaseDir.exists()) {
            for (File f : queryOptionsBaseDir.listFiles()) {
                if (f.getName().endsWith(".xml")) {
                    queryOptions.add(f);
                }
            }
        }
        files.setOptions(queryOptions);
    }

    protected void addTransforms(ConfigurationFiles files, File baseDir) {
        File transformsBaseDir = new File(baseDir, "transforms");
        List<File> transforms = new ArrayList<>();
        if (transformsBaseDir.exists()) {
            for (File f : transformsBaseDir.listFiles()) {
                if (f.getName().endsWith(".xsl")) {
                    transforms.add(f);
                }
            }
        }
        files.setTransforms(transforms);
    }
}

class AssetFilenameFilter implements FilenameFilter {

    @Override
    public boolean accept(File dir, String name) {
        return !name.startsWith(".");
    }

}