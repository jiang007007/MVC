package userTest;

import com.nike.util.CreateJavaBean;
import com.nike.util.Utils;
import org.junit.Test;

import java.io.IOException;

public class JuTest {
//    @Test
//    public void run(){
//        String dir = Utils.createDir();
//        System.out.println(dir);
//    }
    @Test
    public void genterDir(){
        new CreateJavaBean().init();
    }
}
