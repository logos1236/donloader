package donwloader;

import donwloader.view.ViewDownload;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;

@Component
@Scope("prototype")
public class Downloader implements Runnable {
    private URL urlFileFrom;
    private File fileDestination;
    private static int connectionTimeout = 1000;
    private static int readTimeout = 1000;

    public void setUrlFileFrom(URL urlFileFrom) {
        this.urlFileFrom = urlFileFrom;
    }

    public void setFileDestination(File fileDestination) {
        this.fileDestination = fileDestination;
    }

    @Override
    @ViewDownload
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
