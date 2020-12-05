package donwloader;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Donwloader implements Runnable {
    private URL urlFileFrom;
    private File fileDestination;
    private static int connectionTimeout = 1000;
    private static int readTimeout = 1000;

    public Donwloader(File fileTo, URL urlFileFrom) {
        this.urlFileFrom = urlFileFrom;
        this.fileDestination = fileTo;
    }

    @Override
    public void run() {
        try {
            FileUtils.copyURLToFile(
                    urlFileFrom,
                    fileDestination,
                    connectionTimeout,
                    readTimeout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
