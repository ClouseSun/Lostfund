package cn.bit.exec;

import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

/**
 * Created by zhehua on 25/04/2017.
 */
public class TestMakefileTest {
    @Test
    public void execBuild() throws Exception {
        TestMakefile makefile = new TestMakefile("./src/test/resources/raw/makefile");
        Process process = makefile.execBuild("../src/main.c", "../src/main");
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }
}