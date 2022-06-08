package hu.bnpi.databackup;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipBackup {

    private static final int BUFFER = 2048;
    private static final Logger ZIP_BACKUP_LOGGER = (Logger) LoggerFactory.getLogger(ZipBackup.class);

    private final BackupProperties backupProperties;
    private final List<File> listOfFiles = new ArrayList<>();

    public ZipBackup(BackupProperties backupProperties) {
        this.backupProperties = backupProperties;
    }

    public void writeZipBackup() {
        ZIP_BACKUP_LOGGER.info("Starting backup from: {}", backupProperties.getInputFolder());
        createListOfInputFiles();
        listOfFiles.forEach(file -> ZIP_BACKUP_LOGGER.debug("Add file {} to {}", file.getName(),
                backupProperties.getBackupFile().getName().substring(backupProperties.getBackupFile().getName().lastIndexOf("/") + 1)));
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(backupProperties.getBackupFile()))) {
            listOfFiles.forEach(file -> addToZipFile(file, zipOutputStream));
            ZIP_BACKUP_LOGGER.info("{} created successfully with {} bytes.",
                    backupProperties.getBackupFile().getName(), Files.size(backupProperties.getBackupFile().toPath()));
        } catch (IOException | IllegalStateException | IllegalArgumentException exception) {
            ZIP_BACKUP_LOGGER.error("Something went wrong: {}, {}", exception.getClass().toString(), exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void addToZipFile(File file, ZipOutputStream zipOutputStream) {
        try (FileInputStream inputStream = new FileInputStream(file.getPath())) {
            ZipEntry entry = new ZipEntry(createRelativePathString(file));
            entry.setCreationTime(FileTime.fromMillis(file.lastModified()));
            zipOutputStream.putNextEntry(entry);
            writeInputStreamToOutputStream(zipOutputStream, inputStream);
        } catch (IOException ioe) {
            ZIP_BACKUP_LOGGER.error("Unable to process {}!", file.getName());
            throw new IllegalStateException("Unable to process " + file.getName() + "!", ioe);
        }
    }

    private void writeInputStreamToOutputStream(ZipOutputStream zipOutputStream, FileInputStream inputStream) throws IOException {
        byte[] readBuffer = new byte[BUFFER];
        int amountRead;
        while ((amountRead = inputStream.read(readBuffer)) != -1) {
            zipOutputStream.write(readBuffer, 0, amountRead);
        }
    }

    private String createRelativePathString(File file) {
        return file.getPath().substring(backupProperties.getInputFolder().toString().length() + 1);
    }

    private void createListOfInputFiles() {
        try (Stream<Path> paths = Files.walk(backupProperties.getInputFolder().toPath())) {
            paths
                    .map(Path::toFile)
                    .filter(File::isFile)
                    .filter(file -> !file.isHidden())
                    .filter(file -> !file.getName().startsWith("."))
                    .filter(this::filterByFileName)
                    .forEach(listOfFiles::add);
        } catch (IOException ex) {
            ZIP_BACKUP_LOGGER.error("Cannot read folder: {}!", backupProperties.getInputFolder());
            throw new IllegalStateException("Cannot read folder: " + backupProperties.getInputFolder() + "!");
        }
    }

    private boolean filterByFileName(File file) {
        if (backupProperties.getFileFilter() == null || backupProperties.getFileFilter().isBlank()) {
            return true;
        } else {
            return file.getName().contains(backupProperties.getFileFilter());
        }
    }

    public BackupProperties getBackupProperties() {
        return backupProperties;
    }
}
