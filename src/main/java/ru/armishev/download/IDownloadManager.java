package ru.armishev.download;

import java.util.Set;

public interface IDownloadManager {
    public void download(String fileDestination, String url);
    public void download(String fileDestination, String... url);
    public void download(String fileDestination, Set<String> url);
}
