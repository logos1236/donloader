package donwloader;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloaderBuilder {
    private String fileFrom;
    private String strDestination;

    private URL urlFileFrom;
    private File fileDestination;

    public DownloaderBuilder(String strDestination, String fileFrom) {
        this.fileFrom = fileFrom;
        this.strDestination = strDestination;
    }

    public static Donwloader getDownloader(String fileFrom, String destination) throws IOException {
        return new DownloaderBuilder(fileFrom, destination)
                .createFileFromUrl()
                .createDestinationDirectory()
                .createDestionationUnicFile()
                .build();
    }

    private Donwloader build() throws IOException {
        if (this.urlFileFrom == null) {
            throw new IOException("urlFileFrom is empty");
        } else if (this.fileDestination == null) {
            throw new IOException("fileDestination  is empty");
        } else {
            return new Donwloader(this.fileDestination, this.urlFileFrom);
        }
    }

    private DownloaderBuilder createFileFromUrl() throws MalformedURLException {
        this.urlFileFrom = new URL(this.fileFrom);
        return this;
    }

    /* Создаем уникальное имя файла */
    private DownloaderBuilder createDestionationUnicFile() throws IOException {
        String destinationCatalogPath = "";
        String destinationBaseFileName = "";
        String destinationFileExtension = "";
        StringBuilder changerFileName = new StringBuilder();
        String changerFileNamePart = "(1)";

        if (FilenameUtils.getExtension(this.strDestination).isEmpty()) {
            destinationCatalogPath = new File(this.strDestination).getAbsolutePath();
            destinationBaseFileName = FilenameUtils.getBaseName(urlFileFrom.getFile());
            destinationFileExtension = FilenameUtils.getExtension(urlFileFrom.getFile());
        } else {
            destinationCatalogPath = new File(this.strDestination).getParentFile().getAbsolutePath();
            destinationBaseFileName = FilenameUtils.getBaseName(this.strDestination);
            destinationFileExtension = FilenameUtils.getExtension(this.strDestination);
        }

        String fileNameBuilder = destinationCatalogPath+File.separator+destinationBaseFileName;
        File tmpFileName = new File(new StringBuilder(fileNameBuilder).append(changerFileName).append(".").append(destinationFileExtension).toString());
        while (tmpFileName.exists()) {
            changerFileName.append(changerFileNamePart);
            tmpFileName = new File(new StringBuilder(fileNameBuilder).append(changerFileName).append(".").append(destinationFileExtension).toString());
        }
        tmpFileName.createNewFile();

        this.fileDestination = tmpFileName;

        return this;
    }

    /* Создаем каталог для хранения файлов */
    private DownloaderBuilder createDestinationDirectory() throws IOException {
        File tmpDestinationDirectory;

        if (FilenameUtils.getExtension(this.strDestination).isEmpty()) {
            tmpDestinationDirectory = new File(this.strDestination);
        } else {
            tmpDestinationDirectory = new File(this.strDestination).getParentFile();
        }

        if (!tmpDestinationDirectory.exists() && !tmpDestinationDirectory.mkdirs()) {
            throw new IOException("Can't create destination directory");
        }

        return this;
    }
}
