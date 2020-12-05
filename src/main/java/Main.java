import donwloader.DownloadManager;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        String url1 = "https://www.ecomagazine.com/images/Newsletter/0_2019/Week_11-18-19/birdseyeview_ocean.jpg";
        String url2 = "https://www.aljazeera.com/wp-content/uploads/2019/06/5f77ac61d2ab4d6a86e1aa0b110179c8_18.jpeg";
        String url3 = "https://unctad.org/sites/default/files/inline-images/2020-06-08_World-Oceans-Day_400x196.jpg";

        Set<String> urlList = new HashSet<>();
        urlList.add(url1);
        urlList.add(url2);
        urlList.add(url3);

        String fileDestination = "/home/logos/Java/downloader/src/main/resources/download_files/";

        new DownloadManager(1).download(fileDestination, urlList);
    }
}
