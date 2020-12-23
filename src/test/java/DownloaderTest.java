import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.armishev.Main;
import ru.armishev.download.DownloadManager;
import ru.armishev.download.Downloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class DownloaderTest {
    static Set<String> urlList = new HashSet<>();
    static {
        urlList.add("https://www.ecomagazine.com/images/Newsletter/0_2019/Week_11-18-19/birdseyeview_ocean1.jpg");
        urlList.add("https://www.aljazeera.com/wp-content/uploads/2019/06/5f77ac61d2ab4d6a86e1aa0b110179c8_18.jpeg");
        urlList.add("https://unctad.org/sites/default/files/inline-images/2020-06-08_World-Oceans-Day_400x196.jpg");
    }
    static String strDestination =  Paths.get("src","test","resources", "download_files").toAbsolutePath().toString();
    static File fileDestination =  new File(strDestination);

    @Autowired
    private DownloadManager downloadManager;

    @Value("${logging.file.name}")
    private String test;

    static final Logger log = LoggerFactory.getLogger(DownloaderTest.class);

    @Before
    @After
    public void cleanDir() {
        if (!fileDestination.exists()) {
            fileDestination.mkdirs();
        }

        try {
            FileUtils.cleanDirectory(fileDestination);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void downloadListPositiveTest() {
        downloadManager.download(strDestination, urlList);

        Assert.assertEquals(3, fileDestination.list().length);
    }

    @Test
    public void badUrlTest() {
        String badUrl = "https://unctad.org/sites/default/files/inline-images/2020-06-08_World-Oceans-Day_400x196121.jpg";
        File fileDestinationBadUrl = new File(strDestination+"12345.jpg");
        URL url = null;

        try {
            url = new URL(badUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Downloader downloader = new Downloader();
        downloader.setUrlFileFrom(url);
        downloader.setFileDestination(fileDestinationBadUrl);

        Assertions.assertDoesNotThrow(() -> {
            downloader.startDownload();
        });

        Assert.assertEquals(0, fileDestination.list().length);
    }

    @Test
    public void downloadTwoFilesWithSameName() {
        String urlDownload = "https://unctad.org/sites/default/files/inline-images/2020-06-08_World-Oceans-Day_400x196.jpg";

        downloadManager.download(strDestination, urlDownload);
        downloadManager.download(strDestination, urlDownload);

        Assert.assertEquals(2, fileDestination.list().length);
    }

    @Test
    public void downloadFilesToClosedDir() {
        String urlDownload = "https://unctad.org/sites/default/files/inline-images/2020-06-08_World-Oceans-Day_400x196.jpg";
        String closedStrDestination =  Paths.get("src","test","resources", "cant_write").toAbsolutePath().toString();

        Assertions.assertDoesNotThrow(() -> {
            downloadManager.download(closedStrDestination, urlDownload);
        });
    }

    @Test
    public void testLog() {
        System.out.println(test);

        log.error("3434");
    }
}
