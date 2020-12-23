package ru.armishev.download;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.armishev.Main;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.Semaphore;

@Component
@Scope("prototype")
@PropertySource("classpath:application-test.properties")
public class DownloadManager implements IDownloadManager {
    @Autowired
    private ApplicationContext context;

    private Semaphore semaphore = new Semaphore(1);

    static final Logger log = LoggerFactory.getLogger(Main.class);

    public void setStreamCount(int streamCount) {
        this.semaphore = new Semaphore(streamCount);
    }

    public void download(String fileDestination, String fileFrom) {
        try {
            DownloadManagerThread downloadManagerThread = context.getBean(DownloadManagerThread.class);
            new Thread(downloadManagerThread.initDownloader(fileDestination, fileFrom, semaphore)).start();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void download(String fileDestination, String... url) {
        if (url.length > 0) {
            Arrays.stream(url).forEach(x->{download(fileDestination, x);});
        }
    }

    public void download(String fileDestination, Set<String> url) {
        if (!url.isEmpty()) {
            url.stream().forEach(x->{download(fileDestination, x);});
        }
    }
}
