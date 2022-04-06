package hu.bnpi.databackup;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.LoggerFactory;

public class Main {

    public static void main(String[] args) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.print(context);

        WriteZipBackup writeZipBackup = new WriteZipBackup(args[0], args[1]);
        writeZipBackup.writeZip();
    }
}
