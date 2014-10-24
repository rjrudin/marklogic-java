package com.marklogic.gradle.task.security;

import com.marklogic.gradle.xcc.XccHelper;
import com.marklogic.util.LoggingObject;

/**
 * Holding ground for common operations pertaining to security resources.
 */
public class SecurityHelper extends LoggingObject {

    private XccHelper xccHelper;

    public SecurityHelper(XccHelper xccHelper) {
        this.xccHelper = xccHelper;
    }

    public void createRole(String roleName) {
        String xquery = "declare variable $role external; if (sec:role-exists($role)) then () else sec:create-role($role, (), (), (), ())";
        evaluateAgainstSecurityDatabase(xquery, "role", roleName);
    }

    public void setPrivilegeForRole(String roleName, String privilegeAction, String privilegeKind) {
        String xquery = "declare variable $action external;  declare variable $kind external; declare variable $role-names external; "
                + "sec:privilege-set-roles($action, $kind, (sec:privilege-get-roles($action, $kind), $role-names))";
        evaluateAgainstSecurityDatabase(xquery, "role-names", roleName, "action", privilegeAction, "kind",
                privilegeKind);
    }

    public void removeRoles(String... roles) {
        for (String role : roles) {
            String xquery = "declare variable $role external; if (sec:role-exists($role)) then sec:remove-role($role) else ()";
            evaluateAgainstSecurityDatabase(xquery, "role", role);
        }
    }

    public void removeUsers(String... usernames) {
        for (String username : usernames) {
            String xquery = "declare variable $username external; if (sec:user-exists($username)) then sec:remove-user($username) else ()";
            evaluateAgainstSecurityDatabase(xquery, "username", username);
        }
    }

    public String evaluateAgainstSecurityDatabase(String xquery, String... vars) {
        String v = "(";
        for (int i = 0; i < vars.length; i++) {
            if (i > 0) {
                v += ", ";
            }
            if (i % 2 == 1) {
                v += "'" + vars[i] + "'";
            } else {
                v += "xs:QName('" + vars[i] + "')";
            }
        }
        v += ")";

        String s = "xdmp:eval(\"xquery version '1.0-ml'; "
                + "import module namespace sec = 'http://marklogic.com/xdmp/security' at '/MarkLogic/security.xqy'; "
                + xquery + "\", " + v
                + ", <options xmlns='xdmp:eval'><database>{xdmp:security-database()}</database></options>)";
        return xccHelper.executeXquery(s);
    }
}
