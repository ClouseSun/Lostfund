package cn.bit.ui.component;

import cn.bit.file.FileMappingUtils;
import cn.bit.model.FileNodeEntity;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

/**
 * Created by KlousesSun on 2017/3/27.
 */
public class JsonTreePopMenu extends JPopupMenu{

    public JsonTreePopMenu(JTree jTree) {
        String jsonPopMenuPath;
        switch (((FileNodeEntity) ((DefaultMutableTreeNode) jTree.getLastSelectedPathComponent()).getUserObject()).getNodeType()) {
            case FileNodeEntity.NODE_TYPE_DIR:
                jsonPopMenuPath = "/json/popmenu_dir_hierachy";
                break;
            case FileNodeEntity.NODE_TYPE_FILE:
                jsonPopMenuPath = "/json/popmenu_file_hierachy";
                break;
            case FileNodeEntity.NODE_TYPE_ROOT:
                jsonPopMenuPath = "/json/popmenu_project_hierachy";
                break;
            default:
                jsonPopMenuPath = "/json/popmenu_file_hierachy";
        }
        String jsonPopMenuString = null;
        try {
                jsonPopMenuString = IOUtils.
                    toString(getClass().
                            getResourceAsStream (jsonPopMenuPath), "UTF8");
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        MenuList menuList = new Gson().fromJson(jsonPopMenuString, MenuList.class);
        for (MenuList ml:menuList.getMenuList()) {
            if(ml.getMenuList() == null) {
                JMenuItem jMenuItem = new JMenuItem(ml.getTitle());
                jMenuItem.setName(ml.getName());
                add(jMenuItem);
                bindMenuItemListener(jMenuItem, jTree);
            }
            else {
                JMenu jMenu = new JMenu(ml.getTitle());
                add(jMenu);
                if (ml.getMenuList() != null) {
                    for (MenuList mll : ml.getMenuList()) {
                        buildMenuByList(jMenu, mll, jTree);
                    }
                }
            }
        }
    }


    public void buildMenuByList(JMenu root, MenuList menuList, JTree jTree) {
        if(menuList.getMenuList() == null) {
            JMenuItem jMenuItem = new JMenuItem(menuList.getTitle());
            jMenuItem.setName(menuList.getName());
            root.add(jMenuItem);
            bindMenuItemListener(jMenuItem, jTree);
        }
        else {
            JMenu jMenu = new JMenu(menuList.getTitle());
            for (MenuList ml:menuList.getMenuList()) {
                buildMenuByList(jMenu, ml, jTree);
            }
            root.add(jMenu);
        }
    }

    public void bindMenuItemListener(JMenuItem jMenuItem, JTree jTree) {
        jMenuItem.addActionListener((ActionEvent e) -> {
            switch (jMenuItem.getName()) {
                case "menuitem_addExist_file":
                    JFileChooser jFileChooser = new JFileChooser();
                    jFileChooser.setFileSelectionMode(0);
                    if (jFileChooser.showOpenDialog(null) != 1) {
                        File newFile = jFileChooser.getSelectedFile();
                        FileNodeEntity fileNodeEntity = new FileNodeEntity(newFile.getName(), newFile.getName());
                        fileNodeEntity.setNodeType(FileNodeEntity.NODE_TYPE_FILE);
                        ((DefaultTreeModel)jTree.getModel())
                                .insertNodeInto(new DefaultMutableTreeNode(fileNodeEntity),
                                        (DefaultMutableTreeNode)jTree.getLastSelectedPathComponent(),
                                        ((DefaultMutableTreeNode)jTree.getLastSelectedPathComponent()).getChildCount());

                        String abstractPath = FileMappingUtils.path2String(((DefaultMutableTreeNode)jTree.getLastSelectedPathComponent()).getPath()) + newFile.getName();
                        FileMappingUtils.insertNewMapping("res/raw/test_project", abstractPath, newFile.getPath());
                    } else {
                        return;
                    }
                    break;

                case "menuitem_addExist_folder":
                    break;
                case "menuitem_addCopy_file":
                    break;
                case "menuitem_addCopy_folder":
                    break;
                case "menuitem_delete_folder":
                    break;
                case "menuitem_delete_file":
                    break;
            }
        });
    }

}
