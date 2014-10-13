package com.marklogic.test.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.marklogic.client.helper.DatabaseClientConfig;
import com.marklogic.client.helper.DatabaseClientProvider;
import com.marklogic.client.spring.SimpleDatabaseClientProvider;
import com.marklogic.xcc.template.XccTemplate;

/**
 * Provides a basic configuration for Spring-based tests. Assumes that properties can be found in the gradle.properties
 * file, though that file does not need to exist - this can be subclassed and a different property source can be used.
 * And since this is using Spring's Value annotation, system properties can be used to set all of the property values as
 * well.
 */
@Configuration
@PropertySource({ "file:gradle.properties" })
public class BasicTestConfig {

    @Value("${mlUsername:admin}")
    private String mlUsername;

    @Value("${mlPassword}")
    private String mlPassword;

    @Value("${mlHost:localhost}")
    private String mlHost;

    @Value("${mlRestPort:0}")
    private Integer mlRestPort;

    @Value("${mlXdbcPort:0}")
    private Integer mlXdbcPort;

    @Value("${mlTestRestPort:0}")
    private Integer mlTestRestPort;

    @Value("${mlTestXdbcPort:0}")
    private Integer mlTestXdbcPort;

    /**
     * Has to be static so that Spring instantiates it first.
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        PropertySourcesPlaceholderConfigurer c = new PropertySourcesPlaceholderConfigurer();
        c.setIgnoreResourceNotFound(true);
        return c;
    }

    @Bean
    public DatabaseClientConfig databaseClientConfig() {
        int restPort = (mlTestRestPort != null && mlTestRestPort > 0) ? mlTestRestPort : mlRestPort;
        return new DatabaseClientConfig(mlHost, restPort, mlUsername, mlPassword);
    }

    @Bean
    public DatabaseClientProvider databaseClientProvider() {
        return new SimpleDatabaseClientProvider();
    }

    @Bean
    public XccTemplate xccTemplate() {
        int xdbcPort = (mlTestXdbcPort != null && mlTestXdbcPort > 0) ? mlTestXdbcPort : mlXdbcPort;
        return new XccTemplate(String.format("xcc://%s:%s@%s:%d", mlUsername, mlPassword, mlHost, xdbcPort));
    }

    public String getMlUsername() {
        return mlUsername;
    }

    public String getMlPassword() {
        return mlPassword;
    }

    public String getMlHost() {
        return mlHost;
    }

    public Integer getMlRestPort() {
        return mlRestPort;
    }

    public Integer getMlXdbcPort() {
        return mlXdbcPort;
    }

    public Integer getMlTestRestPort() {
        return mlTestRestPort;
    }

    public Integer getMlTestXdbcPort() {
        return mlTestXdbcPort;
    }
}