package cn.bit.ui.component;

import cn.bit.Context;
import cn.bit.file.AbstractFileTree;
import cn.bit.file.FileMappingUtils;
import cn.bit.model.IteProject;
import cn.bit.ui.frame.NewProjectDialog;
import com.github.cjwizard.WizardSettings;
import com.google.gson.Gson;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by KlousesSun on 2017/3/19.
 */
public class JsonMenuBar extends JMenuBar {
    private MenuList menuList;

    public JsonMenuBar(MenuList menuList, JTree jTree) {
        this.menuList = menuList;

        for (MenuList ml:menuList.getMenuList()) {
            if(ml.getMenuList() == null) {
                JMenuItem jMenuItem = new JMenuItem(menuList.getTitle());
                jMenuItem.setEnabled(menuList.isEnable());
                jMenuItem.setName(menuList.getName());
                bindMenuItemListener(jMenuItem, jTree);
                add(jMenuItem);
            } else {
                JMenu jMenu = new JMenu(ml.getTitle());
                jMenu.setEnabled(ml.isEnable());
                add(jMenu);
                if (ml.getMenuList() != null) {
                    for (MenuList mll : ml.getMenuList()) {
                        buildMenuByList(jMenu, mll, jTree);
                    }
                }
            }
        }
    }

    public JsonMenuBar(String jsonString, JTree jTree) {
        this(new Gson().fromJson(jsonString, MenuList.class), jTree);
    }

    protected void buildMenuByList(JMenu root, MenuList menuList, JTree jTree) {
        if(menuList.getMenuList() == null) {
            JMenuItem jMenuItem = new JMenuItem(menuList.getTitle());
            jMenuItem.setEnabled(menuList.isEnable());
            jMenuItem.setName(menuList.getName());
            bindMenuItemListener(jMenuItem, jTree);
            root.add(jMenuItem);
        } else {
            JMenu jMenu = new JMenu(menuList.getTitle());
            jMenu.setEnabled(menuList.isEnable());
            for (MenuList ml:menuList.getMenuList()) {
                buildMenuByList(jMenu, ml, jTree);
            }
            root.add(jMenu);
        }
    }

    private void bindMenuItemListener(JMenuItem jMenuItem, JTree jTree) {
        jMenuItem.addActionListener(e -> {
            switch (jMenuItem.getName()) {
                case "newProject":
                    NewProjectDialog newProjectDialog = new NewProjectDialog();
                    if(newProjectDialog.isFinished()) {
                        WizardSettings settings = newProjectDialog.getSettings();
                        String newPrjName = settings.get("prjNameField").toString();
                        String newPrjPath = settings.get("prjPathField").toString();
                        if(!newPrjPath.endsWith("/")) {
                            newPrjPath += "/";
                        }
                        File newPrjDir = new File(newPrjPath + newPrjName + "/");
                        if(!newPrjDir.exists()) {
                            if(newPrjDir.mkdirs()) {
                                String newIteFile = newPrjPath + newPrjName + "/" + newPrjName + ".ite";
                                FileMappingUtils.createNewProject(
                                        newPrjName,
                                        Context.defaultPrjXmlPath,
                                        newPrjDir.getPath() + "/");
                                AbstractFileTree abstractFileTree = new AbstractFileTree(newPrjName, newIteFile);
                                IteProject iteProject = new IteProject(abstractFileTree);
                                Context.getOpenProjects().put(newPrjName, iteProject);
                                try {
                                    abstractFileTree.addAll(FileMappingUtils.loadFileMapping(new FileInputStream(newIteFile), true));
                                    ((DefaultTreeModel) jTree.getModel()).insertNodeInto(iteProject.getProjectTree().getProjectTreeRoot(),
                                            ((DefaultMutableTreeNode) jTree.getModel().getRoot()),
                                            ((DefaultMutableTreeNode) jTree.getModel().getRoot()).getChildCount());
                                } catch (FileNotFoundException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    }
                    break;
                case "openProject":
                    JFileChooser jFileChooser = new JFileChooser();
                    jFileChooser.setFileFilter(new FileNameExtensionFilter(null, "ite"));
                    if(jFileChooser.showOpenDialog(null) != JFileChooser.CANCEL_OPTION) {

                    }
                    break;
            }
        });
    }

}
