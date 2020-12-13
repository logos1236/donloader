package ru.armishev.download;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.Semaphore;

@Component
@Scope("prototype")
public class DownloadManager implements IDownloadManager {
    @Autowired
    private DownloaderBuilder downloaderBuilder;

    private Semaphore semaphore = new Semaphore(1);

    public void setStreamCount(int streamCount) {
        this.semaphore = new Semaphore(streamCount);
    }

    public void download(String fileDestination, String fileFrom) {
        new Thread(new DownloadManagerThread(fileDestination, fileFrom)).start();
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

    public class DownloadManagerThread implements Runnable {
        private String fileDestination;
        private String fileFrom;

        public DownloadManagerThread(String fileDestination, String fileFrom) {
            this.fileDestination = fileDestination;
            this.fileFrom = fileFrom;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                IDownloader downloader = downloaderBuilder.getDownloader(fileDestination, fileFrom);
                downloader.startDownload();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }
    }
}
