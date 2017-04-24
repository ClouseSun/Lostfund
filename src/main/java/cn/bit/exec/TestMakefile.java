package cn.bit.exec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhehua on 24/04/2017.
 */
public class TestMakefile extends Makefile {
    public TestMakefile(String pathname) {
        super(pathname);
    }

    public Process execCc(int version) throws IOException {
        Map<String, String> vars = new HashMap();
        vars.put("version", version+"");
        return exec("cc_run", vars);
    }

}
