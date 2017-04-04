package cn.bit.ui.component;

import cn.bit.Context;
import cn.bit.file.FileMappingUtils;
import cn.bit.ui.frame.NewProjectDialog;
import com.github.cjwizard.WizardPage;
import com.github.cjwizard.WizardSettings;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import sun.nio.ch.IOUtil;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by KlousesSun on 2017/3/19.
 */
public class JsonMenuBar extends JMenuBar {
    private MenuList menuList;

    public JsonMenuBar(MenuList menuList) {
        this.menuList = menuList;

        for (MenuList ml:menuList.getMenuList()) {
            if(ml.getMenuList() == null) {
                JMenuItem jMenuItem = new JMenuItem(menuList.getTitle());
                jMenuItem.setEnabled(menuList.isEnable());
                jMenuItem.setName(menuList.getName());
                bindMenuItemListener(jMenuItem);
                add(jMenuItem);
            }
            else {
                JMenu jMenu = new JMenu(ml.getTitle());
                jMenu.setEnabled(ml.isEnable());
                add(jMenu);
                if (ml.getMenuList() != null) {
                    for (MenuList mll : ml.getMenuList()) {
                        buildMenuByList(jMenu, mll);
                    }
                }
            }
        }
    }

    public JsonMenuBar(String jsonString) {
        this(new Gson().fromJson(jsonString, MenuList.class));
    }

    protected void buildMenuByList(JMenu root, MenuList menuList) {
        if(menuList.getMenuList() == null) {
            JMenuItem jMenuItem = new JMenuItem(menuList.getTitle());
            jMenuItem.setEnabled(menuList.isEnable());
            jMenuItem.setName(menuList.getName());
            bindMenuItemListener(jMenuItem);
            root.add(jMenuItem);
        }
        else {
            JMenu jMenu = new JMenu(menuList.getTitle());
            jMenu.setEnabled(menuList.isEnable());
            for (MenuList ml:menuList.getMenuList()) {
                buildMenuByList(jMenu, ml);
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
                        File dir = new File(newPrjPath);
                        if(!dir.exists()) {
                            if(dir.mkdirs()) {
                                File newIteFile = new File(newPrjPath + newPrjName + ".ite");
                                FileMappingUtils.createNewProject(Context.configureFilePath, newPrjName, Context.defaultPrjXmlPath ,newIteFile.getPath());

                            }
                        }
                    }
                    break;
            }
        });
    }

}
