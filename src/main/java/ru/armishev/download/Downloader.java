package ru.armishev.download;

import org.springframework.context.annotation.ScopedProxyMode;
import ru.armishev.view.ViewDownload;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

@Component
@Scope(value = "prototype")
public class Downloader implements IDownloader {
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
    public void startDownload() {
        boolean clearFile = false;

        try {
            FileUtils.copyURLToFile(
                    urlFileFrom,
                    fileDestination,
                    connectionTimeout,
                    readTimeout);
        } catch (IOException e) {
            clearFile = true;
            e.printStackTrace();
        }

        if (clearFile) {
            try {
                Files.deleteIfExists(fileDestination.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
