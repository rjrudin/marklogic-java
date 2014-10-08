package com.marklogic.client.configurer;

import java.io.File;
import java.util.List;

public class Modules {

    private List<File> services;
    private List<Asset> assets;
    private List<File> transforms;
    private List<File> options;

    public List<File> getServices() {
        return services;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public List<File> getTransforms() {
        return transforms;
    }

    public List<File> getOptions() {
        return options;
    }

    public void setServices(List<File> resources) {
        this.services = resources;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    public void setTransforms(List<File> transforms) {
        this.transforms = transforms;
    }

    public void setOptions(List<File> queryOptions) {
        this.options = queryOptions;
    }

}
