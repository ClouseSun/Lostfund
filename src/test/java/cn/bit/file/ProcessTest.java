package cn.bit.file;

import org.junit.Test;

import java.io.File;

/**
 * Created by zhehua on 07/04/2017.
 */
public class ProcessTest {
    @Test
    public void commandTest() throws Exception {
        Process p = new ProcessBuilder()
                .directory(new File("/Users/zhehua"))
                .command("ls", "-l")
                .inheritIO()
                .start();
    }
}
