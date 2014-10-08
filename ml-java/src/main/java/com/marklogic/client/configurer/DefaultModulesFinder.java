package com.marklogic.client.configurer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class DefaultModulesFinder implements ModulesFinder {

    private FilenameFilter assetFilenameFilter = new AssetFilenameFilter();
    private FilenameFilter transformFilenameFilter = new TransformFilenameFilter();

    @Override
    public Modules findModules(File baseDir) {
        Modules files = new Modules();
        addServices(files, baseDir);
        addAssets(files, baseDir);
        addOptions(files, baseDir);
        addTransforms(files, baseDir);
        return files;
    }

    protected void addServices(Modules files, File baseDir) {
        File servicesBaseDir = new File(baseDir, "services");
        List<File> services = new ArrayList<>();
        if (servicesBaseDir.exists()) {
            for (File f : servicesBaseDir.listFiles()) {
                if (FilenameUtil.isXqueryFile(f.getName())) {
                    services.add(f);
                }
            }
        }
        files.setServices(services);
    }

    protected void addAssets(Modules files, File baseDir) {
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

    protected void addOptions(Modules files, File baseDir) {
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

    protected void addTransforms(Modules files, File baseDir) {
        File transformsBaseDir = new File(baseDir, "transforms");
        List<File> transforms = new ArrayList<>();
        if (transformsBaseDir.exists()) {
            for (File f : transformsBaseDir.listFiles(transformFilenameFilter)) {
                transforms.add(f);
            }
        }
        files.setTransforms(transforms);
    }

    public FilenameFilter getTransformFilenameFilter() {
        return transformFilenameFilter;
    }

    public void setTransformFilenameFilter(FilenameFilter transformFilenameFilter) {
        this.transformFilenameFilter = transformFilenameFilter;
    }

    public FilenameFilter getAssetFilenameFilter() {
        return assetFilenameFilter;
    }

    public void setAssetFilenameFilter(FilenameFilter assetFilenameFilter) {
        this.assetFilenameFilter = assetFilenameFilter;
    }
}

class AssetFilenameFilter implements FilenameFilter {

    @Override
    public boolean accept(File dir, String name) {
        return !name.startsWith(".");
    }

}

class TransformFilenameFilter implements FilenameFilter {

    @Override
    public boolean accept(File dir, String name) {
        return FilenameUtil.isXslFile(name) || FilenameUtil.isXqueryFile(name);
    }

}