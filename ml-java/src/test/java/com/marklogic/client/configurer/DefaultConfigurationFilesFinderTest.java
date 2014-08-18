package com.marklogic.client.configurer;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class DefaultConfigurationFilesFinderTest extends Assert {

    private ConfigurationFilesFinder sut = new DefaultConfigurationFilesFinder();

    @Test
    public void baseDirWithExtensionsOfEachKind() throws IOException {
        File baseDir = new ClassPathResource("sample-base-dir").getFile();
        ConfigurationFiles files = sut.findConfigurationFiles(baseDir);
        assertEquals(1, files.getOptions().size());
        assertEquals("Only *.xqy files should be included", 1, files.getServices().size());
        assertEquals("Only *.xsl files should be included", 1, files.getTransforms().size());

        List<Asset> assets = files.getAssets();
        assertEquals(2, assets.size());
        assertEquals("/lib/module2.xqy", assets.get(0).getPath());
        assertEquals("/module1.xqy", assets.get(1).getPath());
    }

    @Test
    public void emptyBaseDir() throws IOException {
        File baseDir = new ClassPathResource("empty-base-dir").getFile();
        ConfigurationFiles files = sut.findConfigurationFiles(baseDir);
        assertEquals(0, files.getAssets().size());
        assertEquals(0, files.getOptions().size());
        assertEquals(0, files.getServices().size());
        assertEquals(0, files.getTransforms().size());
    }
}
