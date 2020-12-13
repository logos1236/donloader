package ru.armishev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.armishev.download.DownloadManager;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);

        Set<String> urlList = new HashSet<>();
        urlList.add("https://www.ecomagazine.com/images/Newsletter/0_2019/Week_11-18-19/birdseyeview_ocean1.jpg");
        urlList.add("https://www.aljazeera.com/wp-content/uploads/2019/06/5f77ac61d2ab4d6a86e1aa0b110179c8_18.jpeg");
        urlList.add("https://unctad.org/sites/default/files/inline-images/2020-06-08_World-Oceans-Day_400x196.jpg");


        String absolutePath = Paths.get("src","main","resources").toAbsolutePath().toString();
        String fileDestination = absolutePath+"/download_files/";

        DownloadManager downloadManager = context.getBean(DownloadManager.class);
        downloadManager.setStreamCount(2);
        downloadManager.download(fileDestination, urlList);
    }
}
