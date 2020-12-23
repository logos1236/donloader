package ru.armishev.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.armishev.Main;
import ru.armishev.download.Downloader;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Aspect
@Component
public class ViewData {
    static final Logger log = LoggerFactory.getLogger(Main.class);

    @Pointcut("@annotation(ru.armishev.view.ViewDownload)")
    public void callAtDownload() { }

    @Before("callAtDownload()")
    public void beforeDownload(JoinPoint joinPoint) {
        try {
            Downloader targetObject = (Downloader)joinPoint.getTarget();
            Field urlFileFromField = Downloader.class.getDeclaredField("urlFileFrom");
            urlFileFromField.setAccessible(true);

            log.info("Before "+urlFileFromField.get(targetObject));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
    }

    @After("callAtDownload()")
    public void afterDownload(JoinPoint joinPoint) {
        try {
            Downloader targetObject = (Downloader)joinPoint.getTarget();
            Field urlFileFromField = Downloader.class.getDeclaredField("urlFileFrom");
            urlFileFromField.setAccessible(true);

            log.info("After "+urlFileFromField.get(targetObject));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
    }
}
