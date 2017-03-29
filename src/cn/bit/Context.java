package cn.bit;

import cn.bit.model.IteProject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhehua on 29/03/2017.
 */
public class Context {
    public static final String XML_PATH = "res/raw/test_project";
    public static String configureFilePath = "res/raw/configure";
    List<IteProject> openProjects = new ArrayList<>();

    public Context(List<IteProject> activeProjects) {
        this.openProjects = activeProjects;
    }
}
