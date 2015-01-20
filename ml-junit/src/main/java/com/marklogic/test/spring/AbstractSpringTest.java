package com.marklogic.test.spring;

import java.util.Map;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.marklogic.client.helper.DatabaseClientProvider;
import com.marklogic.test.BaseTestHelper;
import com.marklogic.xcc.template.XccTemplate;

/**
 * Base class for tests that utilize Spring's support for JUnit. Extends BaseTestHelper so that all of the convenience
 * methods available within that are available to subclasses.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ LoggingTestExecutionListener.class, DependencyInjectionTestExecutionListener.class })
public abstract class AbstractSpringTest extends BaseTestHelper implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Before
    public void deleteDocumentsBeforeTestRuns() {
        XccTemplate xccTemplate = getXccTemplate();
        if (xccTemplate != null) {
            xccTemplate.executeXquery(getClearDatabaseXquery());
        } else {
            logger.warn("Not deleting documents before test runs, could not find single instance of "
                    + XccTemplate.class.getName() + " in Spring");
        }
    }

    /**
     * Protected so a subclass can modify this to, e.g., not delete every document.
     */
    protected String getClearDatabaseXquery() {
        return "xdmp:forest-clear(xdmp:database-forests(xdmp:database()))";
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        setDatabaseClientProvider(applicationContext.getBean(DatabaseClientProvider.class));
        Map<String, XccTemplate> map = applicationContext.getBeansOfType(XccTemplate.class);
        if (map.size() == 1) {
            setXccTemplate(map.values().iterator().next());
        }
    }

    protected ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
