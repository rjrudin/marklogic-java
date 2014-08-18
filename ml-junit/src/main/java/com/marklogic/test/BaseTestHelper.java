package com.marklogic.test;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.extensions.ResourceManager;
import com.marklogic.client.helper.DatabaseClientProvider;
import com.marklogic.client.helper.ResourceExtension;
import com.marklogic.test.jdom.MarkLogicNamespaceProvider;
import com.marklogic.test.jdom.NamespaceProvider;
import com.marklogic.xcc.template.XccTemplate;

/**
 * Provides convenience methods for instantiating new TestHelper and ResourceManager implementations. Also extends
 * XmlHelper so that this can be used as a base class for test classes.
 */
public class BaseTestHelper extends XmlHelper implements TestHelper {

    private DatabaseClientProvider clientProvider;
    private XccTemplate xccTemplate;
    private NamespaceProvider namespaceProvider;

    protected BaseTestHelper() {
        namespaceProvider = new MarkLogicNamespaceProvider();
    }

    protected <T extends TestHelper> T newHelper(Class<T> clazz) {
        T helper = null;
        try {
            helper = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        helper.setDatabaseClientProvider(getClientProvider());
        if (xccTemplate != null) {
            helper.setXccTemplate(xccTemplate);
        }
        helper.setNamespaceProvider(getNamespaceProvider());
        return helper;
    }

    protected <T extends ResourceManager> T newResource(Class<T> clazz) {
        T resource = null;
        try {
            resource = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (resource instanceof ResourceExtension) {
            ((ResourceExtension) resource).setDatabaseClient(clientProvider.getDatabaseClient());
        }
        return resource;
    }

    @Override
    public void setDatabaseClientProvider(DatabaseClientProvider provider) {
        this.clientProvider = provider;
    }

    @Override
    public void setXccTemplate(XccTemplate xccTemplate) {
        this.xccTemplate = xccTemplate;
    }

    @Override
    public void setNamespaceProvider(NamespaceProvider namespaceProvider) {
        this.namespaceProvider = namespaceProvider;
    }

    protected NamespaceProvider getNamespaceProvider() {
        return this.namespaceProvider;
    }

    protected DatabaseClient getClient() {
        return clientProvider.getDatabaseClient();
    }

    protected DatabaseClientProvider getClientProvider() {
        return clientProvider;
    }

    protected XccTemplate getXccTemplate() {
        return xccTemplate;
    }
}
