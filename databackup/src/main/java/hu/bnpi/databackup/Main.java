package hu.bnpi.databackup;

public class Main {

    private static BackupProperties backupProperties;

    public static void main(String[] args) {

        if (args.length == 0) {
            backupProperties = new BackupProperties("src/main/resources/default.properties");
        } else if (args.length == 1) {
            backupProperties = new BackupProperties(args[0]);
        } else {
            backupProperties = new BackupProperties(args);
        }

        ZipBackup zipBackup = new ZipBackup(backupProperties);
        zipBackup.writeZipBackup();
    }
}
