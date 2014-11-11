package com.marklogic.gradle.task.cpf;

import com.marklogic.gradle.xcc.XccHelper;
import com.marklogic.util.LoggingObject;

public class CpfHelper extends LoggingObject {

    private XccHelper xccHelper;
    private String triggersDatabaseName;

    public CpfHelper(XccHelper xccHelper, String triggersDatabaseName) {
        this.xccHelper = xccHelper;
        this.triggersDatabaseName = triggersDatabaseName;
    }

    public void installMarkLogicPipeline(String filename) {
        evaluateAgainstTriggersDatabase("p:insert(xdmp:document-get('" + filename + "'))");
    }

    public String evaluateAgainstTriggersDatabase(String xquery, String... vars) {
        String v = "(";
        for (int i = 0; i < vars.length; i++) {
            String var = vars[i];
            if (i > 0) {
                v += ", ";
            }
            if (i % 2 == 1) {
                if (var.startsWith("(") || var.startsWith("<")) {
                    v += vars[i];
                } else {
                    v += "'" + vars[i] + "'";
                }
            } else {
                v += "xs:QName('" + var + "')";
            }
        }
        v += ")";

        String s = "xdmp:eval(\"xquery version '1.0-ml'; "
                + "import module namespace dom = 'http://marklogic.com/cpf/domains' at '/MarkLogic/cpf/domains.xqy'; "
                + "import module namespace p = 'http://marklogic.com/cpf/pipelines' at '/MarkLogic/cpf/pipelines.xqy'; "
                + xquery + "\", " + v + ", <options xmlns='xdmp:eval'><database>{xdmp:database('" + triggersDatabaseName
                + "')}</database></options>)";
        return xccHelper.executeXquery(s);
    }

    public XccHelper getXccHelper() {
        return xccHelper;
    }
}
