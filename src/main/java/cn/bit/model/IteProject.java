package cn.bit.model;

import cn.bit.exec.ExecUtils;
import cn.bit.exec.TestMakefile;
import cn.bit.file.AbstractFileTree;
import cn.bit.file.AbstractLogFileTree;
import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

import java.io.*;
import java.util.Map;

/**
 * Created by zhehua on 29/03/2017.
 */
public class IteProject {

    private AbstractFileTree projectTree;
    private AbstractLogFileTree logTree;
    private TestMakefile makefile;
    private Map<String, DefaultTreeTableModel> execModels;

    private String projectName;
    private String projectConfigPath;

    public void newVersion(InputStream defaultXml) throws IOException, DocumentException {
        int newVersionIndex = execModels.size();

        Document prjDoc = new SAXReader().read(new FileInputStream(projectConfigPath));
        Document dftDoc = new SAXReader().read(defaultXml);

        Element defaultExecRoot = dftDoc.getRootElement().element("defaultExecStatus");
        Element cloneExecRoot = defaultExecRoot.createCopy();
        cloneExecRoot.setQName(new QName("execStatus"));
        cloneExecRoot.addAttribute("version", "ver_" + String.valueOf(newVersionIndex));
        prjDoc.getRootElement().add(cloneExecRoot);

        DefaultTreeTableModel newVersionModel = ExecUtils.loadTestExec(cloneExecRoot);
        execModels.put("ver_" + String.valueOf(newVersionIndex), newVersionModel);

        File prjPath = new File(projectConfigPath).getParentFile();
        File[] verDir = new File(prjPath + "/src").listFiles();
        File newVerDir;
        File srcVerDir = null;
        for(File file : verDir) {
            if(file.getName().startsWith("ver_")) {
                srcVerDir = file;
                break;
            }
        }
        if(srcVerDir != null) {
            newVerDir = new File(srcVerDir.getParent() + "/ver_" + newVersionIndex);
            FileUtils.copyDirectory(srcVerDir,newVerDir);
        }

        XMLWriter xmlWriter = new XMLWriter(new FileWriter(projectConfigPath), OutputFormat.createPrettyPrint());
        xmlWriter.write(prjDoc);
        xmlWriter.close();
    }

    public Map<String, DefaultTreeTableModel> getExecModels() {
        return execModels;
    }

    public void setExecModels(Map<String, DefaultTreeTableModel> execModels) {
        this.execModels = execModels;
    }

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

    public TestMakefile getMakefile() {
        return makefile;
    }

    public void setMakefile(TestMakefile makefile) {
        this.makefile = makefile;
    }

    public AbstractLogFileTree getLogTree() {
        return logTree;
    }

    public IteProject(AbstractFileTree projectTree, AbstractLogFileTree logTree, Map execModels) {
      this.projectTree = projectTree;
      this.logTree = logTree;
      this.execModels = execModels;
   }

    public AbstractFileTree getProjectTree() {
      return projectTree;
   }
}
