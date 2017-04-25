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
        Map<String, String> vars = new HashMap<>();
        vars.put("VERSION", version);
        return exec("cc_run", vars);
    }

    public Process execCdc(String version) throws IOException {
        Map<String, String> vars = new HashMap<>();
        vars.put("VERSION", version);
        return exec("cdc_run", vars);
    }

    public Process execSta() throws IOException {
        Map<String, String> vars = new HashMap<>();
        return exec("pt", vars);
    }

    public Process execSim(String version, String testcase) throws IOException {
        Map<String, String> vars = new HashMap<>();
        vars.put("version", version);
        vars.put("tc", testcase);
        vars.put("cov", "no");
        vars.put("gui", "no");
        vars.put("debug", "no");
        vars.put("sim_tool", "qsim");
        vars.put("sm", "func");
        return exec("sim", vars);
    }

    public Process execSimRgs(String version) throws IOException {
        Map<String, String> vars = new HashMap<>();
        vars.put("version", version);
        vars.put("sm", "func");
        return exec("sim_rgs", vars);
    }

    public Process execVerdi(String version, String testcase) throws IOException {
        Map<String, String> vars = new HashMap<>();
        vars.put("version", version);
        vars.put("tc", testcase);
        return exec("verdi", vars);
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
