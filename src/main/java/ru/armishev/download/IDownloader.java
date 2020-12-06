package ru.armishev.download;

import java.io.File;
import java.net.URL;

public interface IDownloader {
    void startDownload();
    void setFileDestination(File fileDestination);
    void setUrlFileFrom(URL urlFileFrom);
}
