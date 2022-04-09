package hu.bnpi.databackup;

public class Main {

    public static void main(String[] args) {
        BackupProperties backupProperties = new BackupProperties(args);

        ZipBackup zipBackup = new ZipBackup(backupProperties);
        zipBackup.writeZipBackup();
    }
}
