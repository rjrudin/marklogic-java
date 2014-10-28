package com.marklogic.test.spring;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.helper.DatabaseClientProvider;
import com.marklogic.client.modulesloader.ModulesLoader;
import com.marklogic.client.modulesloader.impl.DefaultModulesLoader;

/**
 * Used to automatically load new/modified modules before a test runs.
 */
public class ModulesLoaderTestExecutionListener extends AbstractTestExecutionListener {

    private static boolean initialized = false;
    private final static Logger logger = LoggerFactory.getLogger(ModulesLoaderTestExecutionListener.class);

    /**
     * This currently only runs once; the thought is that an application will have an a base test
     * class that defines the module loaders for all subclasses. Could easily modify this to instead
     * keep track of which directories it has loaded already.
     */
    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        if (!initialized) {
            List<ModulesPath> pathList = null;

            ModulesPaths paths = testContext.getTestClass().getAnnotation(ModulesPaths.class);
            if (paths != null) {
                pathList = Arrays.asList(paths.paths());
            } else {
                ModulesPath path = testContext.getTestClass().getAnnotation(ModulesPath.class);
                if (path != null) {
                    pathList = Arrays.asList(path);
                }
            }

            if (pathList != null) {
                ModulesLoader modulesLoader = buildModulesLoader(testContext);

                DatabaseClientProvider p = testContext.getApplicationContext().getBean(DatabaseClientProvider.class);
                DatabaseClient client = p.getDatabaseClient();

                for (ModulesPath loader : pathList) {
                    String baseDir = loader.baseDir();
                    if (logger.isInfoEnabled()) {
                        logger.info(String.format("Loading modules, using base directory of %s", baseDir));
                    }
                    Set<File> loadedModules = modulesLoader.loadModules(new File(baseDir), client);
                    if (loadedModules != null) {
                        testContext.getApplicationContext().publishEvent(new ModulesLoadedEvent(loadedModules));
                    }
                }
                initialized = true;
            }
        }
    }

    /**
     * Will first check for a ModulesLoader in the Spring container.
     * 
     * @param testContext
     * @return
     */
    protected ModulesLoader buildModulesLoader(TestContext testContext) {
        Map<String, DefaultModulesLoader> loaders = testContext.getApplicationContext().getBeansOfType(
                DefaultModulesLoader.class);
        if (loaders.size() == 1) {
            String name = loaders.keySet().iterator().next();
            logger.info("Found ModulesLoader with name " + name + ", will use that for loading modules");
            return loaders.get(name);
        }
        return new DefaultModulesLoader();
    }
}
