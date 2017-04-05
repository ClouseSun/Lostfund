package cn.bit.ui.component;

import cn.bit.Context;
import cn.bit.file.FileMappingUtils;
import cn.bit.model.FileNodeEntity;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by KlousesSun on 2017/3/27.
 */
public class JsonTreePopMenu extends JPopupMenu{

    public JsonTreePopMenu(JTree jTree) {
        String jsonPopMenuPath;
        switch (((FileNodeEntity) ((DefaultMutableTreeNode) jTree.getLastSelectedPathComponent()).getUserObject()).getNodeType()) {
            case FileNodeEntity.NODE_TYPE_DIR:
                jsonPopMenuPath = Context.getJsonDirPopMenuPath();
                break;
            case FileNodeEntity.NODE_TYPE_FILE:
                jsonPopMenuPath = Context.getJsonFilePopMenuPath();
                break;
            case FileNodeEntity.NODE_TYPE_ROOT:
                jsonPopMenuPath = Context.getJsonProjectPopMenuPath();
                break;
            default:
                jsonPopMenuPath = Context.getJsonFilePopMenuPath();
        }
        String jsonPopMenuString = null;
        try {
                jsonPopMenuString = IOUtils.
                    toString(new FileInputStream(jsonPopMenuPath));
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
        } else {
            JMenu jMenu = new JMenu(menuList.getTitle());
            for (MenuList ml:menuList.getMenuList()) {
                buildMenuByList(jMenu, ml, jTree);
            }
            root.add(jMenu);
        }
    }

    private void addFile(File newFile, Map newFilesMap, DefaultTreeModel treeModel, DefaultMutableTreeNode parentNode) {
        if (newFile.isDirectory()) {
            FileNodeEntity fileNodeEntity = new FileNodeEntity(newFile.getName(), newFile.getName());
            fileNodeEntity.setNodeType(FileNodeEntity.NODE_TYPE_DIR);
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(fileNodeEntity);
            newNode.setAllowsChildren(true);
            treeModel.insertNodeInto(newNode, parentNode, parentNode.getChildCount());
            TreeNode[] newPath = Arrays.copyOfRange(parentNode.getPath(), 2, parentNode.getPath().length);
            String abstractPath = FileMappingUtils.path2String(newPath, FileNodeEntity.NODE_TYPE_DIR)
                    + newFile.getName() + "/";
            newFilesMap.put(abstractPath, newFile.getPath());

            File[] childFileList = newFile.listFiles();
            for(int i = 0; i < childFileList.length; i++) {
                addFile(childFileList[i], newFilesMap, treeModel, newNode);
            }
        } else {
            FileNodeEntity fileNodeEntity = new FileNodeEntity(newFile.getName(), newFile.getName());
            fileNodeEntity.setNodeType(FileNodeEntity.NODE_TYPE_FILE);
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(fileNodeEntity);
            newNode.setAllowsChildren(false);
            treeModel.insertNodeInto(newNode, parentNode, parentNode.getChildCount());
            TreeNode[] newPath = Arrays.copyOfRange(parentNode.getPath(), 2, parentNode.getPath().length);
            String abstractPath = FileMappingUtils.path2String(newPath, FileNodeEntity.NODE_TYPE_DIR)
                    + newFile.getName();
            newFilesMap.put(abstractPath, newFile.getPath());
            return ;
        }
    }

    private void addCopyFile(File sourceFile,
                             Map newFilesMap,
                             DefaultTreeModel treeModel,
                             DefaultMutableTreeNode parentNode) {
        if (sourceFile.isDirectory()) {
            FileNodeEntity fileNodeEntity = new FileNodeEntity(sourceFile.getName(), sourceFile.getName());
            fileNodeEntity.setNodeType(FileNodeEntity.NODE_TYPE_DIR);
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(fileNodeEntity);
            newNode.setAllowsChildren(true);
            treeModel.insertNodeInto(newNode, parentNode, parentNode.getChildCount());

            TreeNode[] newPath = Arrays.copyOfRange(parentNode.getPath(), 2, parentNode.getPath().length);

            String abstractPath = FileMappingUtils.path2String(newPath, FileNodeEntity.NODE_TYPE_DIR)
                    + sourceFile.getName();
            String dirPathToInsert = ((FileNodeEntity) (parentNode).getUserObject()).getRealPath();

            File newFile = new File(dirPathToInsert + sourceFile.getName());
            try {
                IOUtils.copy(new FileInputStream(sourceFile), new FileOutputStream(newFile));
                File[] childFileList = newFile.listFiles();
                for(int i = 0; i < childFileList.length; i++) {
                    addCopyFile(childFileList[i], newFilesMap, treeModel, newNode);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            FileNodeEntity fileNodeEntity = new FileNodeEntity(sourceFile.getName(), sourceFile.getName());
            fileNodeEntity.setNodeType(FileNodeEntity.NODE_TYPE_FILE);
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(fileNodeEntity);
            newNode.setAllowsChildren(false);
            treeModel.insertNodeInto(newNode, parentNode, parentNode.getChildCount());
            TreeNode[] newPath = Arrays.copyOfRange(parentNode.getPath(), 2, parentNode.getPath().length);
            String abstractPath = FileMappingUtils.path2String(newPath, FileNodeEntity.NODE_TYPE_DIR)
                    + sourceFile.getName();
            String dirPathToInsert = ((FileNodeEntity) (parentNode).getUserObject()).getRealPath();
            newFilesMap.put(abstractPath, sourceFile.getPath());
            File newFile = new File(dirPathToInsert + sourceFile.getName());
            try {
                IOUtils.copy(new FileInputStream(sourceFile), new FileOutputStream(newFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ;
        }
    }

    private void bindMenuItemListener(JMenuItem jMenuItem, JTree jTree) {
        jMenuItem.addActionListener((ActionEvent e) -> {
            String parentPath = ((FileNodeEntity) ((DefaultMutableTreeNode) jTree.getLastSelectedPathComponent()).getUserObject()).getRealPath();
            JFileChooser jFileChooser = new JFileChooser(parentPath);
            String projectName = ((DefaultMutableTreeNode) jTree.getLastSelectedPathComponent()).getPath()[1].toString();

            String xmlPath = Context.getProjectFilePath(projectName);
            switch (jMenuItem.getName()) {
                case "menuitem_addExisting":
                    jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    jFileChooser.setMultiSelectionEnabled(true);


                    if (jFileChooser.showOpenDialog(null) != JFileChooser.CANCEL_OPTION) {
                        File[] newFiles = jFileChooser.getSelectedFiles();
                        Map<String, String> newFilesMap = new HashMap<>();
                        for (File newFile : newFiles) {
                            addFile(newFile,
                                    newFilesMap,
                                    ((DefaultTreeModel) jTree.getModel()),
                                    ((DefaultMutableTreeNode) jTree.getLastSelectedPathComponent()));
                        }

                        FileMappingUtils.insertNewMapping(xmlPath, newFilesMap);
                    } else {
                        return;
                    }
                    break;
                case "menuitem_addCopyExisting":
                    jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    jFileChooser.setMultiSelectionEnabled(true);
                    if (jFileChooser.showOpenDialog(null) != 1) {
                        File[] sourceFiles = jFileChooser.getSelectedFiles();
                        Map<String, String> newFilesMap = new HashMap<>();
                        for (File sourceFile : sourceFiles) {
                            addCopyFile(sourceFile,
                                    newFilesMap,
                                    ((DefaultTreeModel) jTree.getModel()),
                                    ((DefaultMutableTreeNode) jTree.getLastSelectedPathComponent()));
                        }
                        FileMappingUtils.insertNewMapping(xmlPath, newFilesMap);
                    } else {
                        return;
                    }
                    break;
                case "menuitem_delete":
                    int confirmRet = JOptionPane.showConfirmDialog(null, "是否删除真实目录下的文件？");

                    if (confirmRet != JOptionPane.CANCEL_OPTION) {
                        int itemType = ((FileNodeEntity) ((DefaultMutableTreeNode) jTree.getLastSelectedPathComponent()).getUserObject()).getNodeType();
                        List<String> removedList = new LinkedList<>();
                        DefaultMutableTreeNode removedNode = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
                        TreeNode[] delPath = Arrays.copyOfRange(removedNode.getPath(), 2, removedNode.getPath().length);
                        removedList.add(FileMappingUtils.path2String(delPath, itemType));
                        FileMappingUtils.removeMappingFromXml(xmlPath,
                                removedList,
                                JOptionPane.OK_OPTION == confirmRet);
                        ((DefaultTreeModel) jTree.getModel()).removeNodeFromParent(removedNode);
                    }
                    break;
                case "menuitem_close":
                    DefaultMutableTreeNode selectedProject = ((DefaultMutableTreeNode) jTree.getLastSelectedPathComponent());
                    FileMappingUtils.closeProject(selectedProject.toString());
                    ((DefaultTreeModel) jTree.getModel()).removeNodeFromParent(selectedProject);
            }
        });
    }

}
