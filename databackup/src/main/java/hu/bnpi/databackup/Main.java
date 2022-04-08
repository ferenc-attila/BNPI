package hu.bnpi.databackup;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        Properties properties = new Properties();
        try (InputStream inputStream = Main.class.getResourceAsStream("/autobackup.properties")) {
            properties.load(inputStream);
        } catch (IOException ioe) {
            throw new IllegalStateException("Cannot load properties file", ioe);
        }
        String dataFolder = properties.getProperty("dataFolder");
        String backupFilePrefix = properties.getProperty("backupFilePrefix");
        String fileFilter = properties.getProperty("fileFilter");

        WriteZipBackup writeZipBackup = new WriteZipBackup(dataFolder, backupFilePrefix, fileFilter);
        writeZipBackup.writeZip();
    }
}
