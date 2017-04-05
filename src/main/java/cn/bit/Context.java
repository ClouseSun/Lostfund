package cn.bit;

import cn.bit.file.AbstractFileTree;
import cn.bit.file.FileMappingUtils;
import cn.bit.model.FileNodeEntity;
import cn.bit.model.IteProject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhehua on 29/03/2017.
 */
public class Context {
    static Map<String, IteProject> openProjects;
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

    public static void init(String configPath) {
        Document document = null;
        try {
            document = new SAXReader().read(new FileInputStream(configPath));
            Map<String, IteProject> openProjects = new LinkedHashMap<>();
            List<Element> prjConfigList = document.getRootElement().elements();
            for (Element prjConfig: prjConfigList) {
                AbstractFileTree abstractFileTree = new AbstractFileTree(prjConfig.attributeValue("projectName")
                        , prjConfig.attributeValue("projectFilePath"));
                abstractFileTree.addAll(FileMappingUtils.loadFileMapping(new FileInputStream(prjConfig.attributeValue("projectFilePath")), true));
                openProjects.put(prjConfig.attributeValue("projectName"), new IteProject(abstractFileTree));
            }
            context = new Context(openProjects);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Context(Map<String, IteProject> openProjects) {
        this.openProjects = openProjects;
    }

    public static Map<String, IteProject> getOpenProjects() {
        return openProjects;
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
