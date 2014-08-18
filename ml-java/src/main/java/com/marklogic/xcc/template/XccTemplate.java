package com.marklogic.xcc.template;

import java.net.URI;

import com.marklogic.util.LoggingObject;
import com.marklogic.xcc.ContentSource;
import com.marklogic.xcc.ContentSourceFactory;
import com.marklogic.xcc.Session;
import com.marklogic.xcc.exceptions.RequestException;

public class XccTemplate extends LoggingObject {

    private ContentSource contentSource;

    public XccTemplate(String uri) {
        try {
            contentSource = ContentSourceFactory.newContentSource(new URI(uri));
            logger.info("Connected to " + uri);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String executeXquery(String xquery) {
        return execute(new XqueryCallback(xquery));
    }

    public <T> T execute(XccCallback<T> callback) {
        Session session = contentSource.newSession();
        try {
            return callback.execute(session);
        } catch (RequestException re) {
            throw new RuntimeException(re);
        } finally {
            session.close();
        }
    }
}
