package com.marklogic.test.spring;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.configurer.ml7.RestApiModulesLoader;
import com.marklogic.client.helper.DatabaseClientProvider;

/**
 * Processes Configurers and Configurer annotations to configure an application via a RestApiModulesLoader.
 */
public class ConfigurerTestExecutionListener extends AbstractTestExecutionListener {

    private static boolean initialized = false;
    private final static Logger logger = LoggerFactory.getLogger(ConfigurerTestExecutionListener.class);

    /**
     * This currently only runs once; the thought is that an application will have an a base test class that defines the
     * configurers for all subclasses. Could easily modify this to instead keep tracking of which directories it has
     * configured already.
     */
    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        if (!initialized) {
            List<Configurer> list = null;

            Configurers cs = testContext.getTestClass().getAnnotation(Configurers.class);
            if (cs != null) {
                list = Arrays.asList(cs.configurers());
            } else {
                Configurer c = testContext.getTestClass().getAnnotation(Configurer.class);
                if (c != null) {
                    list = Arrays.asList(c);
                }
            }

            if (list != null) {
                DatabaseClientProvider p = testContext.getApplicationContext().getBean(DatabaseClientProvider.class);
                DatabaseClient client = p.getDatabaseClient();
                for (Configurer c : list) {
                    String baseDir = c.baseDir();
                    if (logger.isInfoEnabled()) {
                        logger.info(String.format("Configuring application, using base directory of %s", baseDir));
                    }
                    Set<File> loadedModules = new RestApiModulesLoader(client).loadModules(new File(baseDir));
                    if (loadedModules != null) {
                        testContext.getApplicationContext().publishEvent(new ModulesLoadedEvent(loadedModules));
                    }
                }
                initialized = true;
            }
        }
    }

}
