package hu.bnpi.databackup;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class WriteZipBackupTest {

    @TempDir
    File temporaryFolder;

    @Test
    void writeZipWithRelativePathTest() {
        WriteZipBackup writeZipBackup = new WriteZipBackup("src/test/resources", temporaryFolder.getPath() + "/backup");
        writeZipBackup.writeZip();
        Path path = writeZipBackup.getInputFolder().toPath().resolve(writeZipBackup.getOutputFileName());
        assertTrue(Files.exists(path));
    }

    @Test
    void writeZipWithAbsolutePathTest() {
        Path inputAbsolutePath = Path.of("src/test/resources").toAbsolutePath();
        Path outputAbsolutePath = Path.of(temporaryFolder.getPath() + "/backup").toAbsolutePath();
        WriteZipBackup writeZipBackup = new WriteZipBackup(inputAbsolutePath.toString(), outputAbsolutePath.toString());
        writeZipBackup.writeZip();
        Path path = writeZipBackup.getInputFolder().toPath().resolve(writeZipBackup.getOutputFileName());
        assertTrue(Files.exists(path));
    }

    @Test
    void writeZipInputPathNotExistsTest() {
        WriteZipBackup writeZipBackup = new WriteZipBackup("src/test/__resources", temporaryFolder.getPath() + "/backup");
        IllegalStateException ise = assertThrows(IllegalStateException.class, writeZipBackup::writeZip);
        assertEquals("Input folder: src\\test\\__resources does not exists or not a directory!", ise.getMessage());
    }

    @Test
    void writeZipNullInputPathTest() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new WriteZipBackup(null, temporaryFolder.getPath() + "/backup"));
        assertEquals("The input parameters cannot be null or empty string!", iae.getMessage());
    }

    @Test
    void writeZipNullOutputPathTest() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new WriteZipBackup("src/test/__resources", null));
        assertEquals("The input parameters cannot be null or empty string!", iae.getMessage());
    }

    @Test
    void writeZipNullBothPathTest() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new WriteZipBackup(null, null));
        assertEquals("The input parameters cannot be null or empty string!", iae.getMessage());
    }

    @Test
    void writeZipEmptyInputPathTest() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new WriteZipBackup("", temporaryFolder.getPath() + "/backup"));
        assertEquals("The input parameters cannot be null or empty string!", iae.getMessage());
    }

    @Test
    void writeZipEmptyOutputPathTest() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new WriteZipBackup("src/test/__resources", ""));
        assertEquals("The input parameters cannot be null or empty string!", iae.getMessage());
    }

    @Test
    void writeZipEmptyBothPathTest() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new WriteZipBackup("", ""));
        assertEquals("The input parameters cannot be null or empty string!", iae.getMessage());
    }

    @Test
    void writeZipInvalidInputPathTest() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new WriteZipBackup("src/test/:resources", temporaryFolder.getPath() + "/backup"));
        assertEquals("Invalid character in path: ':'!", iae.getMessage());
    }
}