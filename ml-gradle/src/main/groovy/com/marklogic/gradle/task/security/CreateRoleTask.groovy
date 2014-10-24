package com.marklogic.gradle.task.security

import org.gradle.api.tasks.TaskAction;

import com.marklogic.gradle.MarkLogicTask;

class CreateRoleTask extends SecurityTask {

    String roleName
    String[] privilegeActions
    String[] privilegeKinds
    boolean removeRole = true

    @TaskAction
    void createRole() {
        SecurityHelper h = getSecurityHelper()
        if (removeRole) {
            h.removeRoles(roleName)
        }
        h.createRole(roleName)
        for (int i = 0; i < privilegeActions.length; i++) {
            h.setPrivilegeForRole(roleName, privilegeActions[i], privilegeKinds[i])
        }
    }
}
