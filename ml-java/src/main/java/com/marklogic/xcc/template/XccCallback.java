package com.marklogic.xcc.template;

import com.marklogic.xcc.Session;
import com.marklogic.xcc.exceptions.RequestException;

public interface XccCallback<T> {

    public T execute(Session session) throws RequestException;
}
