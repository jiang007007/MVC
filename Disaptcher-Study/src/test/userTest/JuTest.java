package userTest;

import com.nike.application.ActionApplication;
import com.nike.util.CreateJavaBeanUtil;
import com.nike.util.DataSourcePool;
import com.nike.util.JDBCUtil;
import org.junit.Test;

import javax.rmi.CORBA.Util;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JuTest {
//    @Test
//    public void run(){
//        String dir = Utils.createDir();
//        System.out.println(dir);
//    }
    @Test
    public void  testConntection() throws InterruptedException {

        for (int i=0; i< 12;i++){
            new Thread(() -> {
                DataSourcePool instance = DataSourcePool.getInstance();
                System.out.println(instance);
            }).start();
        }
        System.out.println(Thread.currentThread().getName());
        Thread.currentThread().join();
    }

    @Test
    public void  testCreateJavaBean(){
        CreateJavaBeanUtil createJavaBean = new CreateJavaBeanUtil();
        createJavaBean.init();
        createJavaBean.execGenerate();
    }

    @Test
    public void annotationSacnner(){
        ActionApplication ap = new ActionApplication();
        try {
//            ap.init("scannbases.properties");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void  testClass(){
        Properties properties = new Properties();
        try {
        properties.load(JuTest.class.getClassLoader().getResourceAsStream("scannbases.properties"));
            String fixPath =new File("").getCanonicalPath()+File.separator+"target"+File.separator+"classes"+ File.separator
                    +properties.getProperty("basepackage").replace(".",File.separator);
            System.out.println(new File(fixPath).isDirectory());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void UrlClassLoader() throws Exception {
        ActionApplication.scannerController("scannbases.properties");

    }
}
