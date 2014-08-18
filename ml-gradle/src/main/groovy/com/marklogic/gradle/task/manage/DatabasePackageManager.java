package com.marklogic.gradle.task.manage;

import java.util.ArrayList;
import java.util.List;

import com.marklogic.gradle.RestHelper;
import com.marklogic.util.LoggingObject;

public class DatabasePackageManager extends LoggingObject {

    private String appName;

    public DatabasePackageManager(String appName) {
        this.appName = appName;
    }

    public List<String> install(RestHelper rh, String contentDatabaseFilePath, boolean includeTestDatabase,
            String format) {
        List<String> databaseNames = new ArrayList<>();

        String packageName = getPackageName();
        rh.addDatabase(packageName, getContentDatabaseName(), contentDatabaseFilePath, format);
        databaseNames.add(getContentDatabaseName());

        if (includeTestDatabase) {
            rh.addDatabase(packageName, getTestContentDatabaseName(), contentDatabaseFilePath, format);
            databaseNames.add(getTestContentDatabaseName());
        }

        rh.installPackage(packageName, format);

        return databaseNames;
    }

    public String getPackageName() {
        return appName + "-package";
    }

    public String getContentDatabaseName() {
        return appName + "-content";
    }

    public String getTestContentDatabaseName() {
        return appName + "-test-content";
    }
}
