package com.marklogic.client.configurer.ml7;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.DatabaseClientFactory.Authentication;

public class ConfigurationFilesWatcher {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationFilesWatcher.class);

    public static void main(String[] args) throws Exception {
        String commaDelimitedPaths = args[0];
        String host = args[1];
        int port = Integer.parseInt(args[2]);
        String username = args[3];
        String password = args[4];

        logger.info(String.format("Connecting to http://%s:%d as user %s", host, port, username));
        final DatabaseClient client = DatabaseClientFactory.newClient(host, port, username, password,
                Authentication.DIGEST);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.info("Releasing client");
                client.release();
            }
        });

        RestApiConfigurer configurer = new RestApiConfigurer(client);
        List<File> dirs = new ArrayList<>();
        for (String path : commaDelimitedPaths.split(",")) {
            File dir = new File(path);
            dirs.add(dir);
            logger.info("Watching directory: " + dir.getAbsolutePath());
        }

        while (true) {
            if (logger.isTraceEnabled()) {
                logger.trace("Auto-installing any modified files");
            }
            for (File dir : dirs) {
                configurer.loadModules(dir);
            }
            Thread.sleep(1000);
        }
    }
}
