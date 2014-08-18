package com.marklogic.test;

import java.util.List;

import com.marklogic.client.helper.ClientHelper;
import com.marklogic.client.io.StringHandle;
import com.marklogic.test.jdom.Fragment;

/**
 * Intended to provide some common assertion methods that depend on calls to MarkLogic via the MarkLogic Java API - i.e.
 * the DatabaseClient, which is abbreviated as "Client".
 */
public class ClientTestHelper extends BaseTestHelper {

    public Fragment parseUri(String uri, String... expectedCollections) {
        String xml = getClient().newXMLDocumentManager().read(uri, new StringHandle()).get();
        if (expectedCollections != null) {
            assertInCollections(uri, expectedCollections);
        }
        return new Fragment(uri, xml, getNamespaceProvider().getNamespaces());
    }

    public void assertInCollections(String uri, String... collections) {
        List<String> colls = new ClientHelper(getClient()).getCollections(uri);
        for (String c : collections) {
            assertTrue(format("Expected URI %s to be in collection %s", uri, c), colls.contains(c));
        }
    }

    public void assertNotInCollections(String uri, String... collections) {
        List<String> colls = new ClientHelper(getClient()).getCollections(uri);
        for (String c : collections) {
            assertFalse(format("Expected URI %s to not be in collection %s", uri, c), colls.contains(c));
        }
    }

    public void assertCollectionSize(String message, String collection, int size) {
        assertEquals(message, size, new ClientHelper(getClient()).getCollectionSize(collection));
    }

    public List<String> getUrisInCollection(String collectionName, int expectedCount) {
        List<String> uris = new ClientHelper(getClient()).getUrisInCollection(collectionName, expectedCount + 1);
        assertEquals(format("Expected %d uris in collection %s", expectedCount, collectionName), expectedCount,
                uris.size());
        return uris;
    }

}
