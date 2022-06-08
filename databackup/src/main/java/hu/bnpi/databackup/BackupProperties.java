package hu.bnpi.databackup;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class BackupProperties {

    private static final Logger BACKUP_PROPERTIES_LOGGER = (Logger) LoggerFactory.getLogger(BackupProperties.class);

    private File inputFolder;
    private File backupFile;
    private String fileFilter;

    public BackupProperties(String inputFolder, String backupFilePrefix) {
        validateInputString(inputFolder);
        validateInputString(backupFilePrefix);
        this.inputFolder = Path.of(inputFolder).toFile();
        this.backupFile = Path.of(getOutputFileName(backupFilePrefix)).toFile();
        validateInputFolder();
    }

    public BackupProperties(String inputFolder, String backupFilePrefix, String fileFilter) {
        this(inputFolder, backupFilePrefix);
        this.fileFilter = fileFilter;
    }

    public BackupProperties(String[] args) {
        setAttributesByArray(args);
    }

    public BackupProperties(String pathOfPropertiesFile) {
        Properties properties = getProperties(pathOfPropertiesFile);
        String[] arguments = getArgumentsFromProperties(properties);
        setAttributesByArray(arguments);
    }

    private void setAttributesByArray(String[] args) {
        validateArguments(args);
        this.inputFolder = Path.of(args[0]).toFile();
        this.backupFile = Path.of(getOutputFileName(args[1])).toFile();
        if (args.length == 3) {
            this.fileFilter = args[2];
        }
        validateInputFolder();
    }

    private void validateInputFolder() {
        if (!inputFolder.isDirectory()) {
            BACKUP_PROPERTIES_LOGGER.error("Input folder: {} does not exists or not a directory!", inputFolder);
            throw new IllegalStateException("Input folder: " + inputFolder + " does not exists or not a directory!");
        }
    }

    private void validateInputString(String string) {
        if (string == null || string.isBlank()) {
            BACKUP_PROPERTIES_LOGGER.error("The input parameters cannot be null or empty string!");
            throw new IllegalArgumentException("The input parameters cannot be null or empty string!");
        }
    }

    private Properties getProperties(String pathOfPropertiesFile) {
        Properties properties = new Properties();
        try (Reader reader = Files.newBufferedReader(Path.of(pathOfPropertiesFile))) {
            properties.load(reader);
        } catch (IOException ioe) {
            BACKUP_PROPERTIES_LOGGER.error("Cannot load properties file: {}", pathOfPropertiesFile);
            throw new IllegalStateException("Cannot load properties file: " + pathOfPropertiesFile, ioe);
        }
        return properties;
    }

    private String[] getArgumentsFromProperties(Properties properties) {
        String inputFolderProperty = properties.getProperty("inputFolder");
        String backupFilePrefixProperty = properties.getProperty("backupFilePrefix");
        String fileFilterProperty = properties.getProperty("fileFilter", "");
        if (fileFilterProperty == null || fileFilterProperty.isBlank()) {
            String[] result = {inputFolderProperty, backupFilePrefixProperty};
            validateArguments(result);
            return result;
        } else {
            String[] result = {inputFolderProperty, backupFilePrefixProperty, fileFilterProperty};
            validateArguments(result);
            return result;
        }
    }

    private void validateArguments(String[] args) {
        validateInputString(args[0]);
        validateInputString(args[1]);
    }

    public String getOutputFileName(String backupFilePrefix) {
        return backupFilePrefix + createOutputFileNamePostfix();
    }

    private String createOutputFileNamePostfix() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        return "_" + LocalDateTime.now().format(formatter) + ".zip";
    }

    public File getInputFolder() {
        return inputFolder;
    }

    public File getBackupFile() {
        return backupFile;
    }

    public String getFileFilter() {
        return fileFilter;
    }
}
