package donwloader.view;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ViewData {
    @Pointcut("@annotation(donwloader.view.ViewDownload)")
    public void callAtDownload() { }

    @Before("callAtDownload()")
    public void beforeCallAtMethod1(JoinPoint jp) {
        System.out.println("Before");

        /*String args = Arrays.stream(jp.getArgs())
                .map(a -> a.toString())
                .collect(Collectors.joining(","));
        logger.info("before " + jp.toString() + ", args=[" + args + "]");*/
    }

    @After("callAtDownload()")
    public void afterCallAtMethod1(JoinPoint jp) {
        System.out.println("After");

        /*String args = Arrays.stream(jp.getArgs())
                .map(a -> a.toString())
                .collect(Collectors.joining(","));
        logger.info("before " + jp.toString() + ", args=[" + args + "]");*/
    }
}
