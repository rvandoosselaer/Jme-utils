package com.rvandoosselaer.jmeutils;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * A singleton helper implementation that loads a properties file and exposes some methods to retrieve type casted
 * values from the file.
 * This class will load the file specified in the 'application.configurationFile' property. When this property isn't
 * found, it will try to load an 'application.properties' file in the root of the classpath.
 *
 *  Example:
 *  -Dapplication.configurationFile=/home/user/custom.properties
 *  This will try to find the file '/home/user/custom.properties' on the filesystem.
 *
 * @author remy
 */
@Slf4j
public class ApplicationProperties {

    private static final String ENV_APPLICATION_FILE = "application.configurationFile";
    private static final String DEFAULT_FILE = "/application.properties";

    private static ApplicationProperties ourInstance = new ApplicationProperties();

    private final Properties properties;

    private ApplicationProperties() {
        properties = new Properties();
        String environmentFile = System.getProperty(ENV_APPLICATION_FILE);
        if (environmentFile != null) {
            log.info("Found {} property. Loading {}", ENV_APPLICATION_FILE, environmentFile);
            if (!Files.exists(Paths.get(environmentFile))) {
                log.warn("File {} could not be resolved. Using default {}", environmentFile, DEFAULT_FILE);
                environmentFile = null;
            }
        } else {
            log.info("No {} property found, loading default {}", ENV_APPLICATION_FILE, DEFAULT_FILE);
        }
        try (InputStream in = environmentFile != null ? Files.newInputStream(Paths.get(environmentFile)) : getClass().getResourceAsStream(DEFAULT_FILE)) {
            properties.load(in);
        } catch (Exception e) {
            log.error("Error loading {}: {}", environmentFile != null ? environmentFile : DEFAULT_FILE, e.getMessage(), e);
        }
    }

    public static ApplicationProperties getInstance() {
        return ourInstance;
    }

    public String get(String key, String fallback) {
        return properties != null ? properties.getProperty(key, fallback) : fallback;
    }

    public boolean get(String key, boolean fallback) {
        return properties != null ? Boolean.parseBoolean(properties.getProperty(key, String.valueOf(fallback))) : fallback;
    }

    public int get(String key, int fallback) {
        return properties != null ? Integer.parseInt(properties.getProperty(key, String.valueOf(fallback))) : fallback;
    }

}
