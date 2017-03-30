package cn.bit;

import cn.bit.file.AbstractFileTree;
import cn.bit.file.FileMappingUtils;
import cn.bit.model.FileNodeEntity;
import cn.bit.model.IteProject;
import cn.bit.ui.Main;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by zhehua on 29/03/2017.
 */
public class Context {
    Map<String, IteProject> openProjects;
    static Context context;

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
            Map<String, IteProject> openProjects = new HashMap<>();
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

    public Map<String, IteProject> getOpenProjects() {
        return openProjects;
    }

    public static String getProjectFilePath(String projectName) {
        if (context != null) {
            return ((FileNodeEntity) context.openProjects.get(projectName).getProjectTree().getProjectTreeRoot().getUserObject()).getRealName();
        } else {
            throw new IllegalStateException("Context not initialized.");
        }
    }
}
