package com.marklogic.gradle.xcc

import com.marklogic.xcc.AdhocQuery;
import com.marklogic.xcc.ContentSource;
import com.marklogic.xcc.ContentSourceFactory;
import com.marklogic.xcc.Session;
import com.marklogic.xcc.exceptions.RequestException;

class XccHelper {

    String uri
    ContentSource contentSource
    boolean printXquery = false
        
    XccHelper(String uri) {
        this.uri = uri
        println "Connecting to XDBC server at ${uri}"
        this.contentSource = ContentSourceFactory.newContentSource(new URI(uri))
    }
    
    String executeXquery(String xquery) {
        Session session = contentSource.newSession()
        try {
            if (printXquery) {
                println "Executing XQuery: " + xquery
            }
            AdhocQuery q = session.newAdhocQuery(xquery)
            return session.submitRequest(q).asString()
        } catch (RequestException re) {
            throw new RuntimeException(re) 
        } finally {
            session.close()
        }
    }
}
