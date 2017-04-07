package cn.bit.exec;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by zhehua on 07/04/2017.
 */
public class Makefile extends File {

    public Makefile(String pathname) {
        super(pathname);
    }

    public Process exec(String args, Map<String, String> vars) throws IOException {
        StringBuilder sb = new StringBuilder();
        vars.entrySet().stream()
                .forEach(entry -> sb.append(entry.getKey()).append("=").append(entry.getValue()).append(" "));
        Process process = new ProcessBuilder()
                .directory(this.getParentFile())
                .command("make", args, sb.toString())
                .start();
        return process;
    }

    public static class Builder {
        Makefile makefile;

        public Builder(String path) {
            makefile = new Makefile(path + "Makefile");
        }

        public Makefile build() {
            return makefile;
        }
    }
}
