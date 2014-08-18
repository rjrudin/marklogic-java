package com.marklogic.test.jdom;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Namespace;

public class MarkLogicNamespaceProvider implements NamespaceProvider {

    @Override
    public Namespace[] getNamespaces() {
        return buildListOfNamespaces().toArray(new Namespace[] {});
    }

    protected List<Namespace> buildListOfNamespaces() {
        List<Namespace> list = new ArrayList<>();
        list.add(Namespace.getNamespace("prop", "http://marklogic.com/xdmp/property"));
        list.add(Namespace.getNamespace("search", "http://marklogic.com/appservices/search"));
        list.add(Namespace.getNamespace("sem", "http://marklogic.com/semantics"));
        return list;
    }
}
