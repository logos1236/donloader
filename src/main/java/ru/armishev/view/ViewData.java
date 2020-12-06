package ru.armishev.view;

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
    @Pointcut("@annotation(ru.armishev.view.ViewDownload)")
    public void callAtDownload() { }

    @Before("callAtDownload()")
    public void beforeDownload(JoinPoint joinPoint) {
        try {
            Downloader targetObject = (Downloader)joinPoint.getTarget();
            Field urlFileFromField = Downloader.class.getDeclaredField("urlFileFrom");
            urlFileFromField.setAccessible(true);

            System.out.println("Before "+urlFileFromField.get(targetObject));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @After("callAtDownload()")
    public void afterDownload(JoinPoint joinPoint) {
        try {
            Downloader targetObject = (Downloader)joinPoint.getTarget();
            Field urlFileFromField = Downloader.class.getDeclaredField("urlFileFrom");
            urlFileFromField.setAccessible(true);

            System.out.println("After "+urlFileFromField.get(targetObject));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
