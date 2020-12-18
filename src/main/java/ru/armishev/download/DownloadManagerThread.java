package ru.armishev.download;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Semaphore;

@Component
@Scope(value = "prototype")
public class DownloadManagerThread implements Runnable {
    @Autowired
    private IDownloader downloader;

    private Semaphore semaphore;
    private URL fileFrom;
    private File fileDestination;

    public DownloadManagerThread initDownloader(String strDestination, String fileFrom, Semaphore semaphore) throws IOException {
        return this
                .setSemaphore(semaphore)
                .createFileFromUrl(fileFrom)
                .createDestionationUnicFile(strDestination)
                .build();
    }

    private DownloadManagerThread setSemaphore(Semaphore semaphore) {
        this.semaphore = semaphore;

        return this;
    }

    private DownloadManagerThread createFileFromUrl(String fileFrom) throws MalformedURLException {
        this.fileFrom = new URL(fileFrom);
        return this;
    }

    /* Создаем новый уникальный файл, для записи в него */
    private DownloadManagerThread createDestionationUnicFile(String strDestination) throws IOException {
        String destinationCatalogPath = "";
        String destinationBaseFileName = "";
        String destinationFileExtension = "";
        StringBuilder changerFileName = new StringBuilder();
        String changerFileNamePart = "(1)";

        if (FilenameUtils.getExtension(strDestination).isEmpty()) {
            destinationCatalogPath = new File(strDestination).getAbsolutePath();
            destinationBaseFileName = FilenameUtils.getBaseName(fileFrom.getFile());
            destinationFileExtension = FilenameUtils.getExtension(fileFrom.getFile());
        } else {
            destinationCatalogPath = new File(strDestination).getParentFile().getAbsolutePath();
            destinationBaseFileName = FilenameUtils.getBaseName(strDestination);
            destinationFileExtension = FilenameUtils.getExtension(strDestination);
        }

        String fileNameBuilder = destinationCatalogPath+File.separator+destinationBaseFileName;
        File tmpFileName = new File(new StringBuilder(fileNameBuilder).append(changerFileName).append(".").append(destinationFileExtension).toString());
        while (tmpFileName.exists()) {
            changerFileName.append(changerFileNamePart);
            tmpFileName = new File(new StringBuilder(fileNameBuilder).append(changerFileName).append(".").append(destinationFileExtension).toString());
        }

        if (!tmpFileName.createNewFile()) {
            throw new IOException("Can't create file");
        }

        createDestinationDirectory(tmpFileName.getParentFile());

        this.fileDestination = tmpFileName;

        return this;
    }

    /* Создаем каталог для хранения файлов */
    private void createDestinationDirectory(File tmpDestinationDirectory) throws IOException {
        if (!tmpDestinationDirectory.exists() && !tmpDestinationDirectory.mkdirs()) {
            throw new IOException("Can't create destination directory");
        }
    }

    private DownloadManagerThread build() throws IOException {
        if (this.fileFrom == null) {
            throw new IOException("urlFileFrom is empty");
        } else if (this.fileDestination == null) {
            throw new IOException("fileDestination is empty");
        } else {
            this.downloader.setFileDestination(this.fileDestination);
            this.downloader.setUrlFileFrom(this.fileFrom);
        }

        return this;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
            downloader.startDownload();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }
}
