package cn.bit.exec;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        List<String> commandList = new ArrayList<>();
        commandList.add("make");
        commandList.add(args);
        vars.entrySet()
                .forEach(entry -> commandList.add(entry.getKey() + "=" + entry.getValue()));
        Process process = new ProcessBuilder()
                .directory(this.getParentFile())
                .command(commandList)
                .start();

        return process;
    }
}
