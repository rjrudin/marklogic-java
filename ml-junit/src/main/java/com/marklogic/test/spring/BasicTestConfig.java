package com.marklogic.test.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.marklogic.client.spring.BasicConfig;

/**
 * Extends BasicTestConfig with ports for a test application.
 */
@Configuration
@PropertySource({ "file:gradle.properties" })
public class BasicTestConfig extends BasicConfig {

    @Value("${mlTestRestPort:0}")
    private Integer mlTestRestPort;

    @Value("${mlTestXdbcPort:0}")
    private Integer mlTestXdbcPort;

    @Override
    protected Integer getRestPort() {
        return (mlTestRestPort != null && mlTestRestPort > 0) ? mlTestRestPort : getMlRestPort();
    }

    @Override
    protected Integer getXdbcPort() {
        return (mlTestXdbcPort != null && mlTestXdbcPort > 0) ? mlTestXdbcPort : getMlXdbcPort();
    }

    public Integer getMlTestRestPort() {
        return mlTestRestPort;
    }

    public Integer getMlTestXdbcPort() {
        return mlTestXdbcPort;
    }

}