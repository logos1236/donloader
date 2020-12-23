package ru.armishev.download;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ScopedProxyMode;
import ru.armishev.Main;
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

    static final Logger log = LoggerFactory.getLogger(Main.class);

    public void setUrlFileFrom(URL urlFileFrom) {
        this.urlFileFrom = urlFileFrom;
    }

    public void setFileDestination(File fileDestination) {
        this.fileDestination = fileDestination;
    }

    @Value("${logging.file.name}")
    private String test;

    @Override
    @ViewDownload
    public void startDownload() {
        boolean clearFile = false;

        System.out.println(test);

        try {
            FileUtils.copyURLToFile(
                    urlFileFrom,
                    fileDestination,
                    connectionTimeout,
                    readTimeout);
        } catch (IOException e) {
            clearFile = true;
            //log.error(e.getMessage(), e);
        }

        if (clearFile) {
            try {
                Files.deleteIfExists(fileDestination.toPath());
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
