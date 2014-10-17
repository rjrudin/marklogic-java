package com.marklogic.gradle.xcc

import com.marklogic.util.LoggingObject;
import com.marklogic.xcc.AdhocQuery;
import com.marklogic.xcc.ContentSource;
import com.marklogic.xcc.ContentSourceFactory;
import com.marklogic.xcc.Session;
import com.marklogic.xcc.exceptions.RequestException;

class XccHelper extends LoggingObject {

    String uri
    ContentSource contentSource

    XccHelper(String uri) {
        this.uri = uri
        if (logger.isDebugEnabled()) {
            // This will print the password!
            logger.debug("Connecting to XDBC server at ${uri}")
        }
        this.contentSource = ContentSourceFactory.newContentSource(new URI(uri))
    }

    String executeXquery(String xquery) {
        Session session = contentSource.newSession()
        try {
            if (logger.isInfoEnabled()) {
                logger.info("Executing XQuery: " + xquery)
            }
            AdhocQuery q = session.newAdhocQuery(xquery)
            return session.submitRequest(q).asString()
        } finally {
            session.close()
        }
    }
}
