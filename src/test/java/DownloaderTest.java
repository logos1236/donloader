import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.armishev.download.DownloadManager;

import java.io.File;
import java.io.IOException;
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
    static String absolutePath = Paths.get("src","test","resources").toAbsolutePath().toString();
    static String strDestination =  absolutePath+"/download_files/";
    static File fileDestination =  new File(absolutePath+"/download_files/");

    @Autowired
    private DownloadManager downloadManager;

    @Before
    public void cleanDir() {
        try {
            FileUtils.cleanDirectory(fileDestination);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void DownloadTestPositiveList() {
        downloadManager.setStreamCount(2);
        downloadManager.download(strDestination, urlList);

        Assert.assertEquals(3, fileDestination.list().length);
    }
}
