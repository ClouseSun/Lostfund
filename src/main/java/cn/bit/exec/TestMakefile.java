package cn.bit.exec;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhehua on 24/04/2017.
 */
public class TestMakefile extends Makefile {
    public TestMakefile(String pathname) {
        super(pathname);
    }

    public Process execCc(String version) throws IOException {
        Map<String, String> vars = new HashMap();
        vars.put("version", version + "");
        return exec("cc_run", vars);
    }

    public Process execBuild(String srcPath, String outputPath) throws IOException {
        Map<String, String> vars = new HashMap<>();
        vars.put("src", srcPath);
        vars.put("output", outputPath);
        return exec("build", vars);
    }

    public static class Builder {
        Makefile makefile;

        public Builder(String path) {
            makefile = new TestMakefile(path);
        }

        public Makefile build() throws IOException {
            if (makefile.exists()) {
                makefile.delete();
            }
            makefile.createNewFile();

            makefile.setWritable(true);
            FileOutputStream makefileStream = new FileOutputStream(makefile);
            InputStream templateStream = getClass().getResourceAsStream("/raw/makefile_template");
            byte[] buf = new byte[1024];
            while (templateStream.read(buf) > 0) {
                makefileStream.write(buf);
            }
            makefileStream.close();
            templateStream.close();
            return makefile;
        }
    }

}
