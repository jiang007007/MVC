package userTest;

import com.nike.util.CreateJavaBeanUtil;
import com.nike.util.DataSourcePool;
import org.junit.Test;

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
}
