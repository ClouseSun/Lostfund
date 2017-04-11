package cn.bit.model;

import cn.bit.exec.Makefile;
import cn.bit.file.AbstractFileTree;

/**
 * Created by zhehua on 29/03/2017.
 */
public class IteProject {

    private AbstractFileTree projectTree;
    private Makefile makefile;

    private String projectName;
    private String projectConfigPath;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectConfigPath() {
        return projectConfigPath;
    }

    public void setProjectConfigPath(String projectConfigPath) {
        this.projectConfigPath = projectConfigPath;
    }

    public Makefile getMakefile() {
        return makefile;
    }

    public void setMakefile(Makefile makefile) {
        this.makefile = makefile;
    }

    public IteProject(AbstractFileTree projectTree) {
      this.projectTree = projectTree;
   }

   public AbstractFileTree getProjectTree() {
      return projectTree;
   }
}
