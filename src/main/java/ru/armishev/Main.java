package ru.armishev;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.armishev.download.DownloadManager;
import ru.armishev.download.Downloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);

        Set<String> urlList = new HashSet<>();
        urlList.add("http://www.atorus.ru/public/ator/data/image/SovetyTuristam/2903/1_600x450.jpeg");
        urlList.add("https://www.aljazeera.com/wp-content/uploads/2019/06/5f77ac61d2ab4d6a86e1aa0b110179c8_18.jpeg");
        urlList.add("https://unctad.org/sites/default/files/inline-images/2020-06-08_World-Oceans-Day_400x196.jpg");

        String fileDestination = Paths.get("src","main","resources", "download_files").toAbsolutePath().toString();

        DownloadManager downloadManager = context.getBean(DownloadManager.class);
        downloadManager.setStreamCount(2);
        downloadManager.download(fileDestination, urlList);
    }
}
