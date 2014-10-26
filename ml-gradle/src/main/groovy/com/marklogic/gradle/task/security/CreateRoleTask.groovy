package com.marklogic.gradle.task.security

import org.gradle.api.tasks.TaskAction

class CreateRoleTask extends SecurityTask {

    String roleName
    String description
    String[] roleNames
    String[] permissionRoles
    String[] permissionCapabilities
    String[] collections

    String[] privilegeActionNames
    String[] privilegeKinds

    boolean removeRole = true

    @TaskAction
    void createRole() {
        SecurityHelper h = getSecurityHelper()

        if (removeRole) {
            h.removeRoles(roleName)
        }

        h.createRole(roleName, description, roleNames, permissionRoles, permissionCapabilities, collections)

        if (privilegeActionNames != null) {
            for (int i = 0; i < privilegeActionNames.length; i++) {
                h.setPrivilegeForRole(roleName, "http://marklogic.com/xdmp/privileges/" + privilegeActionNames[i], privilegeKinds[i])
            }
        }
    }
}
