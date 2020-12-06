package donwloader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.Semaphore;

@Component
@Scope("prototype")
public class DownloadManager implements IDownloader {
    @Autowired
    ApplicationContext context;

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

    private class DownloadManagerThread implements Runnable {
        private String fileDestination;
        private String fileFrom;

        public DownloadManagerThread(String fileDestination, String fileFrom) {
            this.fileDestination = fileDestination;
            this.fileFrom = fileFrom;
        }

        @Override
        public void run() {
            try {
                DownloadManager.this.semaphore.acquire();
                Downloader downloader = context.getBean(DownloaderBuilder.class).getDownloader(fileDestination, fileFrom);
                downloader.startDownload();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            } finally {
                DownloadManager.this.semaphore.release();
            }
        }
    }
}
