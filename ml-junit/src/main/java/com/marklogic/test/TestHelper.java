package com.marklogic.test;

import com.marklogic.client.helper.DatabaseClientProvider;
import com.marklogic.test.jdom.NamespaceProvider;
import com.marklogic.xcc.template.XccTemplate;

/**
 * Interface for objects that help with writing tests.
 * <ol>
 * <li>A DatabaseClientProvider facilities accessing a MarkLogic REST API server via the MarkLogic Java API.</li>
 * <li>An XccTemplate typically connects to an XDBC server that points to the same database as the REST API server. The
 * XccTemplate is useful for performing operations not yet exposed via the REST API server.</li>
 * <li>The NamespaceProvider provides an array of JDOM namespaces that can be used in XPath namespaces via JDOM.</li>
 * </ol>
 */
public interface TestHelper {

    public void setDatabaseClientProvider(DatabaseClientProvider provider);

    public void setXccTemplate(XccTemplate xccTemplate);

    public void setNamespaceProvider(NamespaceProvider namespaceProvider);
}
