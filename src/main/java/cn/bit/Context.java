package cn.bit;

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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhehua on 29/03/2017.
 */
public class Context {
    static Map<String, IteProject> openProjects;
    static DefaultTreeModel hierarchyModel;
    static DefaultTreeTableModel execModel;
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

    private static IteProject constructAndOpenPrj(String projConfigPath) {
        AbstractFileTree abstractFileTree;

        Document document = null;
        try {
            document = new SAXReader().read(new FileInputStream(projConfigPath));
            // TODO is this attribute exist?
            String projectName = document.getRootElement().attributeValue("projectName");
            abstractFileTree = new AbstractFileTree(projectName, projConfigPath);
            abstractFileTree.addAll(FileMappingUtils.loadFileMapping(
                    document.getRootElement().element("userMapping"), true));
            IteProject iteProject = new IteProject(abstractFileTree);
            iteProject.setProjectName(projectName);
            iteProject.setProjectConfigPath(projConfigPath);
            openProjects.put(projectName, iteProject);
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
            hierarchyModel = new DefaultTreeModel(new DefaultMutableTreeNode(), true);
            execModel = new DefaultTreeTableModel();

            Document document = new SAXReader().read(new FileInputStream(configPath));
            Map<String, IteProject> openProjects = new LinkedHashMap<>();
            List<Element> prjConfigList = document.getRootElement().elements();
            for (Element prjConfig: prjConfigList) {
                constructAndOpenPrj(prjConfig.attributeValue("projectFilePath"));
            }
            openProjects.entrySet().stream().forEach(stringIteProjectEntry ->
                    hierarchyModel.insertNodeInto(stringIteProjectEntry.getValue().getProjectTree().getProjectTreeRoot(),
                            ((DefaultMutableTreeNode) hierarchyModel.getRoot()),
                            ((DefaultMutableTreeNode) hierarchyModel.getRoot()).getChildCount()));
            context = new Context(openProjects);
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
            Context.getHierarchyModel().insertNodeInto(newIteProject.getProjectTree().getProjectTreeRoot(),
                    ((DefaultMutableTreeNode) Context.getHierarchyModel().getRoot()),
                    ((DefaultMutableTreeNode) Context.getHierarchyModel().getRoot()).getChildCount());

            Document configureDoc = new SAXReader().read(new FileInputStream(Context.configureFilePath));
            Element prjConfigs = configureDoc.getRootElement();
            Element newPrj = prjConfigs.addElement("ActiveProject");
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

    private Context(Map<String, IteProject> openProjects) {
        this.openProjects = openProjects;
    }

    public static Map<String, IteProject> getOpenProjects() {
        return openProjects;
    }

    public static DefaultTreeModel getHierarchyModel() {
        return hierarchyModel;
    }

    public static DefaultTreeTableModel getExecModel() {
        return execModel;
    }

    public static String getProjectFilePath(String projectName) {
        if (context != null) {
            return ((FileNodeEntity) context.openProjects.get(projectName).getProjectTree().getProjectTreeRoot().getUserObject()).getRealPath();
        } else {
            throw new IllegalStateException("Context not initialized.");
        }
    }

    public static String getJsonMenuBarPath() {
        if (context != null) {
            return jsonMenuBarPath;
        } else {
            throw new IllegalStateException("Context not initialized.");
        }
    }

    public static String getJsonDirPopMenuPath() {
        if (context != null) {
            return jsonDirPopMenuPath;
        } else {
            throw new IllegalStateException("Context not initialized.");
        }
    }

    public static String getJsonFilePopMenuPath() {
        if (context != null) {
            return jsonFilePopMenuPath;
        } else {
            throw new IllegalStateException("Context not initialized.");
        }
    }

    public static String getJsonProjectPopMenuPath() {
        if (context != null) {
            return jsonProjectPopMenuPath;
        } else {
            throw new IllegalStateException("Context not initialized.");
        }
    }
}
