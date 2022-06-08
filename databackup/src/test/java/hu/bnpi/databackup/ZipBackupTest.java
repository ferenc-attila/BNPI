package hu.bnpi.databackup;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ZipBackupTest {

    @TempDir
    File temporaryFolder;

    @Test
    void writeZipWithRelativePathTest() {
        ZipBackup zipBackup = new ZipBackup(new BackupProperties("src/test/resources", temporaryFolder.getPath() + "/backup", "gpkg"));
        zipBackup.writeZipBackup();
        Path path = zipBackup.getBackupProperties().getBackupFile().toPath();
        assertTrue(Files.exists(path));
    }

    @Test
    void writeZipWithAbsolutePathTest() {
        Path inputAbsolutePath = Path.of("src/test/resources").toAbsolutePath();
        Path outputAbsolutePath = Path.of(temporaryFolder.getPath() + "/backup").toAbsolutePath();
        ZipBackup zipBackup = new ZipBackup(new BackupProperties(inputAbsolutePath.toString(), outputAbsolutePath.toString(), "gpkg"));
        zipBackup.writeZipBackup();
        Path path = zipBackup.getBackupProperties().getBackupFile().toPath();
        assertTrue(Files.exists(path));
    }

    @Test
    void writeZipWithEmptyFilterTest() {
        Path inputAbsolutePath = Path.of("src/test/resources").toAbsolutePath();
        Path outputAbsolutePath = Path.of(temporaryFolder.getPath() + "/backup").toAbsolutePath();
        ZipBackup zipBackup = new ZipBackup(new BackupProperties(inputAbsolutePath.toString(), outputAbsolutePath.toString(), ""));
        zipBackup.writeZipBackup();
        Path path = zipBackup.getBackupProperties().getBackupFile().toPath();
        assertTrue(Files.exists(path));
    }

    @Test
    void writeZipWithNullFilterTest() {
        Path inputAbsolutePath = Path.of("src/test/resources").toAbsolutePath();
        Path outputAbsolutePath = Path.of(temporaryFolder.getPath() + "/backup").toAbsolutePath();
        ZipBackup zipBackup = new ZipBackup(new BackupProperties(inputAbsolutePath.toString(), outputAbsolutePath.toString()));
        zipBackup.writeZipBackup();
        Path path = zipBackup.getBackupProperties().getBackupFile().toPath();
        assertTrue(Files.exists(path));
    }
}