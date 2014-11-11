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
        evaluateAgainstTriggersDatabase("declare variable $filename external; p:insert(xdmp:document-get($filename))",
                "filename", filename);
    }

    public void createDomain(String name, String description, String scope, String scopeUri, String scopeDepth,
            String modulesDatabaseName, String[] pipelineNames, String[] permissions) {

        String pipelinesXml = "<names>";
        for (String n : pipelineNames) {
            pipelinesXml += "<name>" + n + "</name>";
        }
        pipelinesXml += "</names>";

        String xquery = "declare variable $name external; declare variable $description external; ";
        xquery += "declare variable $scope external; declare variable $scope-uri external; declare variable $scope-depth external; ";
        xquery += "declare variable $modules-database-name external; declare variable $pipelines external; declare variable $permissions external; ";
        xquery += "dom:create($name, $description, ";
        xquery += "dom:domain-scope($scope, $scope-uri, if ($scope-uri = 'directory') then $scope-depth else ()), ";
        xquery += "dom:evaluation-context(xdmp:database($modules-database-name), '/'), ";
        xquery += "for $name in $pipelines/element()/fn:string() return p:get($name)/p:pipeline-id, ";
        xquery += "for $perm in $permissions return xdmp:permission($perm/role, $perm/capability))";

        evaluateAgainstTriggersDatabase(xquery, "name", name, "description", description, "scope", scope, "scope-uri",
                scopeUri, "scope-depth", scopeDepth, "modules-database-name", modulesDatabaseName, "pipelines",
                pipelinesXml, "permissions", buildPermissionsXml(permissions));
    }

    public void createDomainConfiguration(String restartUser, String modulesDatabaseName, String defaultDomainName,
            String[] permissions) {
        // There's not a "does the configuration exist?" function so have to resort to a try/catch
        String xquery = "declare variable $restart-user external; declare variable $modules-database-name external; ";
        xquery += "declare variable $default-domain-name external; declare variable $permissions external; ";
        xquery += "let $domain-id := fn:data(dom:get($default-domain-name)/dom:domain-id)";
        xquery += "return try { dom:configuration-get(), dom:configuration-set-default-domain($domain-id) } ";
        xquery += "catch ($err) {";
        xquery += "dom:configuration-create($restart-user, ";
        xquery += "dom:evaluation-context(xdmp:database($modules-database-name), '/'), ";
        xquery += "fn:data(dom:get($default-domain-name)/dom:domain-id), ";
        xquery += "for $perm in $permissions return xdmp:permission($perm/role, $perm/capability))";

        evaluateAgainstTriggersDatabase(xquery, "restart-user", restartUser, "modules-database-name",
                modulesDatabaseName, "default-domain-name", defaultDomainName, "permissions",
                buildPermissionsXml(permissions));
    }

    private String buildPermissionsXml(String[] permissions) {
        String permissionsXml = "<permissions>";
        for (int i = 0; i < permissions.length; i += 2) {
            permissionsXml += "<permission><role>" + permissions[i] + "</role><capability>" + permissions[i + 1]
                    + "</capability></permission>";
        }
        permissionsXml += "</permissions>";
        return permissionsXml;
    }

    public void removeDomain(String domainName) {
        evaluateAgainstTriggersDatabase("declare variable $domain-name external; dom:remove($domain-name)",
                "domain-name", domainName);
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
                + xquery + "\", " + v + ", <options xmlns='xdmp:eval'><database>{xdmp:database('"
                + triggersDatabaseName + "')}</database></options>)";
        return xccHelper.executeXquery(s);
    }

    public XccHelper getXccHelper() {
        return xccHelper;
    }
}
