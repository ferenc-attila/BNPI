package hu.bnpi.databackup;

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

public class WriteZipBackup {

    private static final int BUFFER = 2048;

    private final File inputFolder;
    private final String outputFileName;
    private final List<File> listOfFiles = new ArrayList<>();


    public WriteZipBackup(String inputFolder, String outputFileName) {
        //validateInputStrings!!!
        this.inputFolder = Path.of(inputFolder).toFile();
        this.outputFileName = outputFileName;
    }

    public void writeZip() {
        createListOfInputFiles();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(outputFileName))) {
            listOfFiles.forEach(file -> addToZipFile(file, zipOutputStream));
        } catch (IOException | IllegalStateException exception) {
            // Something must happens!!!
        }
    }

    private void addToZipFile(File file, ZipOutputStream zipOutputStream) {
        try (FileInputStream inputStream = new FileInputStream(file.getPath())) {
            ZipEntry entry = new ZipEntry(createRelativePathString(file));
            entry.setCreationTime(FileTime.fromMillis(file.lastModified()));
            zipOutputStream.putNextEntry(entry);

            byte[] readBuffer = new byte[BUFFER];
            int amountRead;
            while ((amountRead = inputStream.read(readBuffer)) != -1) {
                zipOutputStream.write(readBuffer, 0, amountRead);
            }
        } catch (IOException ioe) {
            throw new IllegalStateException("Unable to process " + file.getName() + "!", ioe);
        }
    }

    private String createRelativePathString(File file) {
        return file.getPath().substring(inputFolder.toString().length() + 1);
    }

    private void createListOfInputFiles() {
        validateInputFolder();
        try (Stream<Path> paths = Files.walk(inputFolder.toPath())) {
            paths
                    .map(Path::toFile)
                    .filter(File::isFile)
                    .forEach(listOfFiles::add);
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot read folder: " + inputFolder + "!");
        }
    }

    private void validateInputFolder() {
        if (inputFolder == null) {
            throw new IllegalStateException("Folder can not be null!");
        }
        if (!inputFolder.isDirectory()) {
            throw new IllegalStateException("Input folder: " + inputFolder + " does not exists or not a directory!");
        }
    }
}
