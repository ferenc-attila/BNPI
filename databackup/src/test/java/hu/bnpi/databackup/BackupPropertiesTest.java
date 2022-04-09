package hu.bnpi.databackup;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class BackupPropertiesTest {

    @Test
    void createByTwoStringsTest() {
        String separator = System.getProperty("file.separator");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String dateTimeString = LocalDateTime.now().format(formatter);

        BackupProperties backupProperties = new BackupProperties("src/test/resources", "src/test/resources/backup");

        assertEquals("src" + separator + "test" + separator + "resources", backupProperties.getInputFolder().getPath());
        assertEquals("src/test/resources/backup_" + dateTimeString + ".zip", backupProperties.getOutputFileName());
        assertNull(backupProperties.getFileFilter());
    }

    @Test
    void createByThreeStringsTest() {
        String separator = System.getProperty("file.separator");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String dateTimeString = LocalDateTime.now().format(formatter);

        BackupProperties backupProperties = new BackupProperties("src/test/resources", "src/test/resources/backup", "gpkg");

        assertEquals("src" + separator + "test" + separator + "resources", backupProperties.getInputFolder().getPath());
        assertEquals("src/test/resources/backup_" + dateTimeString + ".zip", backupProperties.getOutputFileName());
        assertEquals("gpkg", backupProperties.getFileFilter());
    }

    @Test
    void createByTwoArgumentsTest() {
        String[] args = new String[]{"src/test/resources", "src/test/resources/backup"};
        String separator = System.getProperty("file.separator");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String dateTimeString = LocalDateTime.now().format(formatter);

        BackupProperties backupProperties = new BackupProperties(args);

        assertEquals("src" + separator + "test" + separator + "resources", backupProperties.getInputFolder().getPath());
        assertEquals("src/test/resources/backup_" + dateTimeString + ".zip", backupProperties.getOutputFileName());
        assertNull(backupProperties.getFileFilter());
    }

    @Test
    void createByThreeArgumentsTest() {
        String separator = System.getProperty("file.separator");
        String[] args = new String[]{"src/test/resources", "src/test/resources/backup", "gpkg"};
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String dateTimeString = LocalDateTime.now().format(formatter);

        BackupProperties backupProperties = new BackupProperties(args);

        assertEquals("src" + separator + "test" + separator + "resources", backupProperties.getInputFolder().getPath());
        assertEquals("src/test/resources/backup_" + dateTimeString + ".zip", backupProperties.getOutputFileName());
        assertEquals("gpkg", backupProperties.getFileFilter());
    }

    @Test
    void createBackupPropertiesByPropertiesFileWithFilterTest() {
        String separator = System.getProperty("file.separator");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String dateTimeString = LocalDateTime.now().format(formatter);

        BackupProperties backupProperties = new BackupProperties("src/test/resources/filteredbackup.properties");

        assertEquals("." + separator + "docker_resources" + separator + "data", backupProperties.getInputFolder().getPath());
        assertEquals("./docker_resources/backup/test_backup_" + dateTimeString + ".zip", backupProperties.getOutputFileName());
        assertEquals("gpkg", backupProperties.getFileFilter());
    }

    @Test
    void createBackupPropertiesByPropertiesFileWithoutFilterTest() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String separator = System.getProperty("file.separator");
        String dateTimeString = LocalDateTime.now().format(formatter);

        BackupProperties backupProperties = new BackupProperties("src/test/resources/unfilteredbackup.properties");

        assertEquals("." + separator + "docker_resources" + separator + "data", backupProperties.getInputFolder().getPath());
        assertEquals("./docker_resources/backup/test_backup_" + dateTimeString + ".zip", backupProperties.getOutputFileName());
        assertNull(backupProperties.getFileFilter());
    }

    @Test
    void createBackupPropertiesByNonExistentPropertiesFileTest() {
        IllegalStateException ise = assertThrows(IllegalStateException.class,
                () -> new BackupProperties("src/test/resources/nosuchfile.properties"));
        assertEquals("Cannot load properties file: src/test/resources/nosuchfile.properties", ise.getMessage());
    }

    @Test
    void createBackupPropertiesByFileWithInvalidArguments() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new BackupProperties("src/test/resources/invalid_argument.properties"));
        assertEquals("The input parameters cannot be null or empty string!", iae.getMessage());
    }

    @Test
    void inputPathNotExistsTest() {
        String separator = System.getProperty("file.separator");
        IllegalStateException ise = assertThrows(IllegalStateException.class,
                () -> new BackupProperties("src/test/__resources", "src/test/resources/backup", "gpkg"));
        assertEquals("Input folder: src" + separator + "test" + separator + "__resources does not exists or not a directory!", ise.getMessage());
    }

    @Test
    void nullInputPathTest() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new BackupProperties(null, "src/test/resources/backup", "gpkg"));
        assertEquals("The input parameters cannot be null or empty string!", iae.getMessage());
    }

    @Test
    void nullOutputPathTest() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new BackupProperties("src/test/__resources", null, "gpkg"));
        assertEquals("The input parameters cannot be null or empty string!", iae.getMessage());
    }

    @Test
    void nullBothPathTest() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new BackupProperties(null, null, "gpkg"));
        assertEquals("The input parameters cannot be null or empty string!", iae.getMessage());
    }

    @Test
    void emptyInputPathTest() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new BackupProperties("", "src/test/resources/backup", "gpkg"));
        assertEquals("The input parameters cannot be null or empty string!", iae.getMessage());
    }

    @Test
    void emptyOutputPathTest() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new BackupProperties("src/test/__resources", "", "gpkg"));
        assertEquals("The input parameters cannot be null or empty string!", iae.getMessage());
    }

    @Test
    void emptyBothPathTest() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new BackupProperties("", "", "gpkg"));
        assertEquals("The input parameters cannot be null or empty string!", iae.getMessage());
    }

    @Test
    void invalidCharacterInInputPathTest() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new BackupProperties("src/test/:resources", "src/test/resources/backup", "gpkg"));
        assertEquals("Illegal char <:> at index 9: src/test/:resources", iae.getMessage());
    }
}