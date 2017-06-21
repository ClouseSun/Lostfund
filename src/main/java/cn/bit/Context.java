package cn.bit;

import cn.bit.exec.ExecUtils;
import cn.bit.exec.TestMakefile;
import cn.bit.file.AbstractFileTree;
import cn.bit.file.AbstractLogFileTree;
import cn.bit.file.FileMappingUtils;
import cn.bit.model.FileNodeEntity;
import cn.bit.model.IteProject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.*;
import java.util.*;

/**
 * Created by zhehua on 29/03/2017.
 */
public class Context {
    private Map<String, IteProject> openProjects;
    private IteProject activeProject;

    private DefaultTreeModel projectFileModel;
    private DefaultTreeModel logFileModel;
    private static Context context;

    public final static String RES_ROOT = "src/main/resources/";
    public static String configureFilePath = RES_ROOT + "raw/configure";
    public static String jsonMenuBarPath = RES_ROOT + "json/MenuBar/menubar_hierachy";
    public static String jsonDirPopMenuPath = RES_ROOT + "json/TreePopupMenu/popmenu_dir_hierachy";
    public static String jsonFilePopMenuPath = RES_ROOT + "json/TreePopupMenu/popmenu_file_hierachy";
    public static String jsonProjectPopMenuPath = RES_ROOT + "json/TreePopupMenu/popmenu_project_hierachy";
    public static String defaultPrjXmlPath = RES_ROOT + "raw/default_file_map";

    public static String DEFAULT_MAKEFILE_PATH = "run/Makefile";


    public static Context getContext() {
        if (context != null) {
            return context;
        } else {
            throw new IllegalStateException("Context not initialized.");
        }
    }

    /**
     * Build and open a project by its ITE file.
     * Insert new project into the map of all open projects {@link #openProjects}.
     * @param prjConfigPath the full path of the XML file of the new project.
     * @return the constructed project.
     * */

    public static IteProject constructAndOpenPrj(String prjConfigPath) {
        Document document = null;
        try {
            document = new SAXReader().read(new FileInputStream(prjConfigPath));
            // TODO is this attribute exist?
            String projectName = document.getRootElement().attributeValue("projectName");

            // construct project file tree
            AbstractFileTree projectFileTree = new AbstractFileTree(projectName, prjConfigPath);
            projectFileTree.addAll(FileMappingUtils.loadFileMapping(
                    document.getRootElement().element("userMapping"), true));

            // construct log file tree
            AbstractLogFileTree logFileTree = new AbstractLogFileTree(projectName, prjConfigPath);
            logFileTree.addAll(FileMappingUtils.loadFileMapping(
                    document.getRootElement().element("userLogMapping"), true));

            // construct exec tree
            Map<String, DefaultTreeTableModel> execModels = new TreeMap<>();
            List<Element> execStatusElements = document.getRootElement().elements("execStatus");
            execStatusElements.forEach(element ->  {
                String version = element.attributeValue("version");
                DefaultTreeTableModel model = ExecUtils.loadTestExec(element);
                execModels.put(version, model);
            });

            IteProject iteProject = new IteProject(projectFileTree, logFileTree, execModels);
            iteProject.setProjectName(projectName);
            iteProject.setProjectConfigPath(prjConfigPath);
            String projectPath = new File(prjConfigPath).getParent();
            if (!projectPath.endsWith("/")) {
                projectPath += "/";
            }
            iteProject.setMakefile(new TestMakefile(projectPath + Context.DEFAULT_MAKEFILE_PATH));
            iteProject.setExecModels(execModels);
            context.openProjects.put(projectName, iteProject);
            return iteProject;
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void init(String configPath) {
        try {
            context = new Context();

            Document document = new SAXReader().read(new FileInputStream(configPath));
            List<Element> prjConfigList = document.getRootElement().elements();
            for (Element prjConfig: prjConfigList) {
                IteProject newProject = constructAndOpenPrj(prjConfig.attributeValue("projectFilePath"));
                if(prjConfig.attributeValue("isActivated").equals("true")) {
                    context.activeProject = newProject;
                }
            }
            context.openProjects.entrySet().forEach(stringIteProjectEntry -> {
                context.projectFileModel.insertNodeInto(stringIteProjectEntry.getValue().getProjectTree().getProjectTreeRoot(),
                        ((DefaultMutableTreeNode) context.projectFileModel.getRoot()),
                        ((DefaultMutableTreeNode) context.projectFileModel.getRoot()).getChildCount());

                context.logFileModel.insertNodeInto(stringIteProjectEntry.getValue().getLogTree().getProjectTreeRoot(),
                        ((DefaultMutableTreeNode) context.logFileModel.getRoot()),
                        ((DefaultMutableTreeNode) context.logFileModel.getRoot()).getChildCount());
            });


        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    // TODO test
    /**
     * Open an existing project by .ite file.
     * Insert new node into JTree model and register in configure XML.
     * @param itePath full path of the .ite file.
     * */
    public static void openProject(String itePath) {
        try {
            IteProject newIteProject = constructAndOpenPrj(itePath);
            context.getProjectFileModel().insertNodeInto(newIteProject.getProjectTree().getProjectTreeRoot(),
                    ((DefaultMutableTreeNode) context.getProjectFileModel().getRoot()),
                    ((DefaultMutableTreeNode) context.getProjectFileModel().getRoot()).getChildCount());

            context.getLogFileModel().insertNodeInto(newIteProject.getLogTree().getProjectTreeRoot(),
                    ((DefaultMutableTreeNode) context.getLogFileModel().getRoot()),
                    ((DefaultMutableTreeNode) context.getLogFileModel().getRoot()).getChildCount());

            Document configureDoc = new SAXReader().read(new FileInputStream(Context.configureFilePath));
            Element prjConfigs = configureDoc.getRootElement();
            Element newPrj = prjConfigs.addElement("Project");
            newPrj.addAttribute("projectName", newIteProject.getProjectName());
            newPrj.addAttribute("projectFilePath", itePath);
            XMLWriter configXmlWriter = new XMLWriter(new FileWriter(Context.configureFilePath),
                    OutputFormat.createPrettyPrint());
            configXmlWriter.write(configureDoc);
            configXmlWriter.flush();

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private Context() {
        this.projectFileModel = new DefaultTreeModel(new DefaultMutableTreeNode(), true);
        this.logFileModel = new DefaultTreeModel(new DefaultMutableTreeNode(), true);
        this.openProjects = new LinkedHashMap<>();
    }

    public Map<String, IteProject> getOpenProjects() {
        return openProjects;
    }

    public DefaultTreeModel getProjectFileModel() {
        return projectFileModel;
    }

    public String getProjectFilePath(String projectName) {
        if (context != null) {
            return ((FileNodeEntity) context.openProjects.get(projectName).getProjectTree().getProjectTreeRoot().getUserObject()).
                    getRealPath();
        } else {
            throw new IllegalStateException("Context not initialized.");
        }
    }

    public IteProject getProjectByName(String prjName) {
        if (context != null) {
            return context.openProjects.get(prjName);
        } else {
            throw new IllegalStateException("Context not initialized.");
        }
    }

    public String getJsonMenuBarPath() {
        if (context != null) {
            return jsonMenuBarPath;
        } else {
            throw new IllegalStateException("Context not initialized.");
        }
    }

    public String getJsonDirPopMenuPath() {
        if (context != null) {
            return jsonDirPopMenuPath;
        } else {
            throw new IllegalStateException("Context not initialized.");
        }
    }

    public String getJsonFilePopMenuPath() {
        if (context != null) {
            return jsonFilePopMenuPath;
        } else {
            throw new IllegalStateException("Context not initialized.");
        }
    }

    public String getJsonProjectPopMenuPath() {
        if (context != null) {
            return jsonProjectPopMenuPath;
        } else {
            throw new IllegalStateException("Context not initialized.");
        }
    }

    public DefaultTreeModel getLogFileModel() {
        return logFileModel;
    }

    public IteProject getActiveProject() {
        return activeProject;
    }

    public void setActiveProject(IteProject activeProject) {
        this.activeProject = activeProject;
    }
}
