package com.marklogic.test.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class LoggingTestExecutionListener extends AbstractTestExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger(LoggingTestExecutionListener.class);

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        logger.info("Starting test method: " + testContext.getTestMethod().getName());
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        logger.info("Finished test method: " + testContext.getTestMethod().getName());
    }

}
