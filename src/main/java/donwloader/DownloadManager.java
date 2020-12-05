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
    DownloaderBuilder downloaderBuilder;

    private Semaphore semaphore;

    public DownloadManager(int streamCount) {
        this.semaphore = new Semaphore(streamCount);
    }

    public void download(String fileDestination, String fileFrom) {
        try {
            this.semaphore.acquire();
            /*Downloader downloader = context.getBean(DownloaderBuilder.class)
                    .getDownloader(fileDestination, fileFrom);*/
            Downloader downloader = downloaderBuilder.getDownloader(fileDestination, fileFrom);
            new Thread(downloader).start();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            this.semaphore.release();
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
