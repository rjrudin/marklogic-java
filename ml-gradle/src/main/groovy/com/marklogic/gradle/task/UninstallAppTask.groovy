package com.marklogic.gradle.task

import org.gradle.api.tasks.TaskAction

import com.marklogic.gradle.MarkLogicTask
import com.marklogic.gradle.xcc.XccHelper

class UninstallAppTask extends MarkLogicTask {

    String xccUrl

    @TaskAction
    void uninstallApp() {
        if (!xccUrl) {
            xccUrl = getDefaultXccUrl()
        }
        
        String xquery =
                'import module namespace admin = "http://marklogic.com/xdmp/admin" at "/MarkLogic/admin.xqy"; ' +
                'import module namespace sec = "http://marklogic.com/xdmp/security" at "/MarkLogic/security.xqy"; ' +
                'let \$prefix := "' + getAppName() + '" ' +
                'let \$config := admin:get-configuration() ' +
                'let \$rest-server-name := \$prefix ' +
                'let \$appserver-ids := ' +
                '  for \$name in (\$rest-server-name, fn:concat(\$rest-server-name, "-test"), fn:concat(\$prefix, "-content-xdbc"), fn:concat(\$prefix, "-test-content-xdbc")) ' +
                '  where admin:appserver-exists(\$config, (), \$name) ' +
                '  return admin:appserver-get-id(\$config, (), \$name) ' +
                'let \$database-ids := ' +
                '  for \$name in (fn:concat(\$prefix, "-content"), fn:concat(\$prefix, "-modules"), fn:concat(\$prefix, "-test-content"), fn:concat(\$prefix, "-triggers")) ' +
                '  where admin:database-exists(\$config, \$name) ' +
                '  return admin:database-get-id(\$config, \$name) ' +
                'let \$forest-ids-to-delete := ' +
                '  for \$forest-id in admin:get-forest-ids(\$config) ' +
                '  where admin:forest-get-database(\$config, \$forest-id) = \$database-ids ' +
                '  return \$forest-id ' +
                'let \$documents-id := admin:database-get-id(\$config, "Documents") ' +
                'let \$_ := ' +
                '  for \$id in \$appserver-ids ' +
                '  let \$config := admin:get-configuration() ' +
                '  let \$config := admin:appserver-set-database(\$config, \$id, \$documents-id) ' +
                '  let \$config := admin:appserver-set-modules-database(\$config, \$id, \$documents-id) ' +
                '  return admin:save-configuration(\$config) ' +
                'let \$forest-messages := ' +  
                '  for \$id in \$forest-ids-to-delete ' + 
                '  let \$db-id := admin:forest-get-database(\$config, \$id) ' + 
                '  let \$config := admin:get-configuration() ' + 
                '  let \$config := admin:database-detach-forest(\$config, \$db-id, \$id) ' + 
                '  let \$_ := admin:save-configuration(\$config) ' + 
                '  let \$config := admin:get-configuration() ' + 
                '  let \$message := text{"Deleted forest", admin:forest-get-name(\$config, \$id)} ' + 
                '  let \$_ := xdmp:log(\$message) ' + 
                '  let \$config := admin:forest-delete(\$config, \$id, fn:true()) ' + 
                '  return (admin:save-configuration(\$config), \$message) ' + 
                'let \$database-messages := ' + 
                '  for \$id in \$database-ids ' + 
                '  let \$config := admin:get-configuration() ' + 
                '  let \$message := text{"Deleted database", admin:database-get-name(\$config, \$id)} ' + 
                '  let \$_ := xdmp:log(\$message) ' + 
                '  let \$config := admin:database-delete(\$config, \$id) ' + 
                '  return (admin:save-configuration(\$config), \$message) ' + 
                'let \$appserver-messages := ' +  
                '  for \$id at \$index in \$appserver-ids ' + 
                '  let \$config := admin:get-configuration() ' + 
                '  let \$message := text{"Deleted appserver", admin:appserver-get-name(\$config, \$id)} ' + 
                '  let \$_ := xdmp:log(\$message) ' + 
                '  let \$config := admin:appserver-delete(\$config, \$id) ' + 
                '  return ' + 
                '    if (\$index = fn:count(\$appserver-ids)) then ' +  
                '       let \$_ := admin:save-configuration-without-restart(\$config) ' + 
                '       return \$message ' + 
                '    else (admin:save-configuration(\$config), \$message) ' + 
                'return (\$database-messages, \$forest-messages, \$appserver-messages)'

        println "Uninstalling app with name " + getAppName()
        println new XccHelper(xccUrl).executeXquery(xquery)
    }
}
