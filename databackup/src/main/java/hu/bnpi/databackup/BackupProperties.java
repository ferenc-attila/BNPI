package hu.bnpi.databackup;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BackupProperties {

    private static final Logger BACKUP_PROPERTIES_LOGGER = (Logger) LoggerFactory.getLogger(BackupProperties.class);

    private final String dataFolder;
    private final String backupFilePrefix;
    private String fileFilter;

    public BackupProperties(String dataFolder, String backupFilePrefix) {
        this.dataFolder = dataFolder;
        this.backupFilePrefix = backupFilePrefix;
    }

    public BackupProperties(String dataFolder, String backupFilePrefix, String fileFilter) {
        this(dataFolder, backupFilePrefix);
        this.fileFilter = fileFilter;
    }

    public BackupProperties (String[] args) {
        validateCliArguments(args);
        if (args.length == 2) {
            this.dataFolder = args[0];
            this.backupFilePrefix = args[1];
        } else {
            this.dataFolder = args[0];
            this.backupFilePrefix = args[1];
            this.fileFilter = args[2];
        }
    }

    public BackupProperties createBackupPropertiesByPropertiesFile (String pathOfPropertiesFile) {
        Properties properties = getProperties(pathOfPropertiesFile);
        String dataFolderProperty = properties.getProperty("dataFolder");
        String backupFilePrefixProperty = properties.getProperty("backupFilePrefix");
        String fileFilterProperty = properties.getProperty("fileFilter", "");
        if (fileFilterIsExists(fileFilterProperty)) {
            return new BackupProperties(dataFolderProperty, backupFilePrefixProperty, fileFilterProperty);
        } else {
            return new BackupProperties(dataFolderProperty, backupFilePrefixProperty);
        }
    }

    private Properties getProperties(String pathOfPropertiesFile) {
        Properties properties = new Properties();
        try (InputStream inputStream = Main.class.getResourceAsStream(pathOfPropertiesFile)) {
            properties.load(inputStream);
        } catch (IOException ioe) {
            throw new IllegalStateException("Cannot load properties file", ioe);
        }
        return properties;
    }

    private boolean fileFilterIsExists(String fileFilter) {
        return !(fileFilter == null || fileFilter.isBlank());
    }

    private void validateCliArguments(String[] args) {
        if (args.length < 2 || args.length > 3) {
            BACKUP_PROPERTIES_LOGGER.error("Number of commandline arguments must be 2 or 3!");
            throw new IllegalArgumentException("Number of commandline arguments must be 2 or 3!");
        }
     }

    public String getDataFolder() {
        return dataFolder;
    }

    public String getBackupFilePrefix() {
        return backupFilePrefix;
    }

    public String getFileFilter() {
        return fileFilter;
    }
}
