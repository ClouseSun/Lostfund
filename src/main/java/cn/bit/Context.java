package cn.bit;

import cn.bit.exec.ExecUtils;
import cn.bit.file.AbstractFileTree;
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
    Map<String, IteProject> openProjects;
    IteProject activeProject;

    DefaultTreeModel projectFileModel;
    static Context context;

    public final static String RES_ROOT = "src/main/resources/";
    public static String configureFilePath = RES_ROOT + "raw/configure";
    public static String jsonMenuBarPath = RES_ROOT + "json/MenuBar/menubar_hierachy";
    public static String jsonDirPopMenuPath = RES_ROOT + "json/TreePopupMenu/popmenu_dir_hierachy";
    public static String jsonFilePopMenuPath = RES_ROOT + "json/TreePopupMenu/popmenu_file_hierachy";
    public static String jsonProjectPopMenuPath = RES_ROOT + "json/TreePopupMenu/popmenu_project_hierachy";
    public static String defaultPrjXmlPath = RES_ROOT + "raw/default_file_map";

    public static Context getContext() {
        if (context != null) {
            return context;
        } else {
            throw new IllegalStateException("Context not initialized.");
        }
    }

    public static IteProject constructAndOpenPrj(String prjConfigPath) {

        Document document = null;
        try {
            document = new SAXReader().read(new FileInputStream(prjConfigPath));
            // TODO is this attribute exist?
            String projectName = document.getRootElement().attributeValue("projectName");

            // construct project file tree
            AbstractFileTree abstractFileTree = new AbstractFileTree(projectName, prjConfigPath);
            abstractFileTree.addAll(FileMappingUtils.loadFileMapping(
                    document.getRootElement().element("userMapping"), true));

            // construct exec tree
            Map<String, DefaultTreeTableModel> execModels = new TreeMap<>();
            List<Element> execStatusElements = document.getRootElement().elements("execStatus");
            execStatusElements.stream().forEach(element ->  {
                String version = element.attributeValue("version");
                DefaultTreeTableModel model = ExecUtils.loadTestExec(element);
                execModels.put(version, model);
            });

            IteProject iteProject = new IteProject(abstractFileTree);
            iteProject.setProjectName(projectName);
            iteProject.setProjectConfigPath(prjConfigPath);
            iteProject.setExecModels(execModels);
            context.openProjects.put(projectName, iteProject);
            return iteProject;
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
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
                if(prjConfig.attributeValue("isActivated") == "true") {
                    context.activeProject = newProject;
                }
            }
            context.openProjects.entrySet().stream().forEach(stringIteProjectEntry ->
                    context.projectFileModel.insertNodeInto(stringIteProjectEntry.getValue().getProjectTree().getProjectTreeRoot(),
                            ((DefaultMutableTreeNode) context.projectFileModel.getRoot()),
                            ((DefaultMutableTreeNode) context.projectFileModel.getRoot()).getChildCount())

            );

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
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

            Document configureDoc = new SAXReader().read(new FileInputStream(Context.configureFilePath));
            Element prjConfigs = configureDoc.getRootElement();
            Element newPrj = prjConfigs.addElement("Project");
            newPrj.addAttribute("projectName", newIteProject.getProjectName());
            newPrj.addAttribute("projectFilePath", itePath);
            XMLWriter configXmlWriter = new XMLWriter(new FileWriter(Context.configureFilePath), OutputFormat.createPrettyPrint());
            configXmlWriter.write(configureDoc);
            configXmlWriter.flush();

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Context() {
        this.projectFileModel = new DefaultTreeModel(new DefaultMutableTreeNode(), true);
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

    public IteProject getActiveProject() {
        return activeProject;
    }

    public void setActiveProject(IteProject activeProject) {
        this.activeProject = activeProject;
    }
}
