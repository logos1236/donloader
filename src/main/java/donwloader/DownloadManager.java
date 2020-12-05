package donwloader;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.Semaphore;

public class DownloadManager implements IDownloader {
    private Semaphore semaphore;

    public DownloadManager(int streamCount) {
        this.semaphore = new Semaphore(streamCount);
    }

    public void download(String fileDestination, String fileFrom) {
        try {
            this.semaphore.acquire();
            Donwloader donwloader = DownloaderBuilder.getDownloader(fileDestination, fileFrom);
            new Thread(donwloader).start();
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
