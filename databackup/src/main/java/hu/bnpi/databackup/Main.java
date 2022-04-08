package hu.bnpi.databackup;

public class Main {

    public static void main(String[] args) {
        BackupProperties backupProperties = new BackupProperties(args);

        WriteZipBackup writeZipBackup = new WriteZipBackup(backupProperties.getDataFolder(), backupProperties.getBackupFilePrefix(), backupProperties.getFileFilter());
        writeZipBackup.writeZip();
    }
}
