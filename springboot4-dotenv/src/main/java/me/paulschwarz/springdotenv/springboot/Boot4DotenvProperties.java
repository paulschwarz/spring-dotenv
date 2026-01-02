package me.paulschwarz.springdotenv.springboot;

import static me.paulschwarz.springdotenv.DotenvConfigLoader.SPRINGDOTENV;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.DeprecatedConfigurationProperty;

@ConfigurationProperties(prefix = SPRINGDOTENV)
public class Boot4DotenvProperties {

    /**
     * Enable or disable loading of the .env file.
     */
    private boolean enabled = true;

    /**
     * Directory to search for the .env file (default: the classpath).
     */
    private String directory;

    /**
     * Name of the dotenv file to load (default: ".env").
     */
    private String filename = ".env";

    /**
     * If true, malformed lines in the .env file are ignored instead of failing fast.
     */
    private boolean ignoreIfMalformed = false;

    /**
     * If true, a missing .env file is ignored instead of failing startup.
     */
    private boolean ignoreIfMissing = true;

    /**
     * If true, export loaded .env entries into JVM system properties.
     */
    private boolean exportToSystemProperties = false;
    private boolean exportToSystemPropertiesBound = false;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public boolean isIgnoreIfMalformed() {
        return ignoreIfMalformed;
    }

    public void setIgnoreIfMalformed(boolean ignoreIfMalformed) {
        this.ignoreIfMalformed = ignoreIfMalformed;
    }

    public boolean isIgnoreIfMissing() {
        return ignoreIfMissing;
    }

    public void setIgnoreIfMissing(boolean ignoreIfMissing) {
        this.ignoreIfMissing = ignoreIfMissing;
    }

    public boolean isExportToSystemProperties() {
        return exportToSystemProperties;
    }

    public void setExportToSystemProperties(boolean exportToSystemProperties) {
        this.exportToSystemProperties = exportToSystemProperties;
        this.exportToSystemPropertiesBound = true; // canonical explicitly set
    }

    /**
     * Legacy alias for exportToSystemProperties.
     */
    @Deprecated
    @DeprecatedConfigurationProperty(
        replacement = SPRINGDOTENV + ".export-to-system-properties",
        reason = "Renamed for clarity."
    )
    public boolean isSystemProperties() {
        return exportToSystemProperties;
    }

    @Deprecated
    public void setSystemProperties(boolean systemProperties) {
        // Only apply legacy value if canonical was NOT set
        if (!this.exportToSystemPropertiesBound) {
            this.exportToSystemProperties = systemProperties;
        }
    }
}
