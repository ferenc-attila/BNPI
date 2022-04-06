package hu.bnpi.databackup;

public class Main {

    public static void main(String[] args) {
        WriteZipBackup writeZipBackup = new WriteZipBackup(args[0], args[1]);
        writeZipBackup.writeZip();
    }
}
