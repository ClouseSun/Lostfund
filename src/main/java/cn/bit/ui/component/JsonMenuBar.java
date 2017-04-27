package cn.bit.ui.component;

import cn.bit.Context;
import cn.bit.file.FileMappingUtils;
import cn.bit.model.IteProject;
import cn.bit.model.MenuList;
import cn.bit.ui.frame.NewProjectDialog;
import com.github.cjwizard.WizardSettings;
import com.google.gson.Gson;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;

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
                bindMenuItemListener(jMenuItem);
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
            bindMenuItemListener(jMenuItem);
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

    private void bindMenuItemListener(JMenuItem jMenuItem) {
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
                                FileMappingUtils.createNewProjectXmlAndFiles(
                                        newPrjName,
                                        Context.defaultPrjXmlPath,
                                        newPrjDir.getPath() + "/");

                                IteProject newIteProject = Context.constructAndOpenPrj(newIteFile);
                                DefaultTreeModel projectTreeModel = Context.getContext().getProjectFileModel();
                                Context.getContext().getProjectFileModel().insertNodeInto(
                                        newIteProject.getProjectTree().getProjectTreeRoot(),
                                        ((DefaultMutableTreeNode) projectTreeModel.getRoot()),
                                        ((DefaultMutableTreeNode) projectTreeModel.getRoot()).getChildCount());

                            }
                        }
                    }
                    break;
                case "openProject":
                    JFileChooser jFileChooser = new JFileChooser();
                    jFileChooser.setMultiSelectionEnabled(false);
                    jFileChooser.setFileFilter(new FileNameExtensionFilter(null, "ite"));
                    if(jFileChooser.showOpenDialog(null) != JFileChooser.CANCEL_OPTION) {
                        Context.openProject(jFileChooser.getSelectedFile().getPath());
                    }
                    break;
            }
        });
    }

}