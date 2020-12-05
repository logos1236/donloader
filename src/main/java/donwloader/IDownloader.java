package donwloader;

import java.util.Set;

public interface IDownloader {
    public void download(String fileDestination, String url);
    public void download(String fileDestination, String... url);
    public void download(String fileDestination, Set<String> url);
}
