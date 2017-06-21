package cn.bit.exec;

import org.apache.commons.io.FileUtils;

import java.io.File;
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

    public Process exec(String args) throws IOException {
        return exec(args, null);
    }

//    public Process execCc(String version) throws IOException {
//        Map<String, String> vars = new HashMap<>();
//        vars.put("VERSION", version);
//        return exec("cc_run", vars);
//    }
//
//    public Process execCdc(String version) throws IOException {
//        Map<String, String> vars = new HashMap<>();
//        vars.put("VERSION", version);
//        return exec("cdc_run", vars);
//    }
//
//    public Process execSta() throws IOException {
//        Map<String, String> vars = new HashMap<>();
//        return exec("pt", vars);
//    }
//
//    public Process execSim(String version, String testcase) throws IOException {
//        Map<String, String> vars = new HashMap<>();
//        vars.put("version", version);
//        vars.put("device", "altera");
//        vars.put("sdkv", "8.0");
//        vars.put("dut_name", "rwgl");
//        vars.put("lrtl", "vhdl");
//        vars.put("lnet", "verilog");
//        vars.put("tb", "tb");
//        vars.put("tc_name", "tc_name");
//        vars.put("tc", testcase);
//        vars.put("cov", "no");
//        vars.put("gui", "no");
//        vars.put("debug", "no");
//        vars.put("sim_tool", "qsim");
//        vars.put("sm", "func");
//        vars.put("sdf_type", "typ");
//        return exec("sim", vars);
//    }
//
//    public Process execSimRgs(String version) throws IOException {
//        Map<String, String> vars = new HashMap<>();
//        vars.put("version", version);
//        vars.put("sm", "func");
//        return exec("sim_rgs", vars);
//    }
//
//    public Process execVerdi(String version, String testcase) throws IOException {
//        Map<String, String> vars = new HashMap<>();
//        vars.put("version", version);
//        vars.put("tc", testcase);
//        return exec("verdi", vars);
//    }
//
//    public Process execBuild(String srcPath, String outputPath) throws IOException {
//        Map<String, String> vars = new HashMap<>();
//        vars.put("src", srcPath);
//        vars.put("output", outputPath);
//        return exec("build", vars);
//    }

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
            FileUtils.copyFile(new File("./src/main/resources/raw/makefile_template"), makefile);

            return makefile;
        }
    }

}
