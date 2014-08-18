package com.marklogic.xcc.template;

import com.marklogic.xcc.AdhocQuery;
import com.marklogic.xcc.Session;
import com.marklogic.xcc.exceptions.RequestException;

public class XqueryCallback implements XccCallback<String> {

    private String xquery;

    public XqueryCallback(String xquery) {
        this.xquery = xquery;
    }

    @Override
    public String execute(Session session) throws RequestException {
        AdhocQuery q = session.newAdhocQuery(xquery);
        return session.submitRequest(q).asString();
    }

}
