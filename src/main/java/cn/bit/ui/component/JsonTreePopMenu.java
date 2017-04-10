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
                jMenuItem.setEnabled(ml.isEnable());
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
            jMenuItem.setEnabled(menuList.isEnable());
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

    private void addFile(File newFile,
                         Map newFilesMap,
                         DefaultTreeModel treeModel,
                         DefaultMutableTreeNode parentNode) {
        if (newFile.isDirectory()) {
            FileNodeEntity fileNodeEntity = new FileNodeEntity(newFile.getName(), newFile.getName());
            fileNodeEntity.setNodeType(FileNodeEntity.NODE_TYPE_DIR);
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(fileNodeEntity);
            newNode.setAllowsChildren(true);
            treeModel.insertNodeInto(newNode, parentNode, parentNode.getChildCount());
            TreeNode[] newPath = Arrays.copyOfRange(parentNode.getPath(), 2, parentNode.getPath().length);
            String abstractPath = FileMappingUtils.path2String(newPath, false)
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
            String abstractPath = FileMappingUtils.path2String(newPath, false)
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

            String abstractPath = FileMappingUtils.path2String(newPath, false)
                    + sourceFile.getName();
            String dirPathToInsert = ((FileNodeEntity) (parentNode).getUserObject()).getRealPath();
            newFilesMap.put(abstractPath, sourceFile.getPath());
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
            String abstractPath = FileMappingUtils.path2String(newPath, false)
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
            String projectName = ((DefaultMutableTreeNode) jTree.getLastSelectedPathComponent()).getPath()[1].toString();
            String projectXmlPath = Context.getProjectFilePath(projectName);

            JFileChooser jFileChooser = new JFileChooser(projectXmlPath.substring(0, projectXmlPath.lastIndexOf("/")));


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

                        FileMappingUtils.insertNewMapping(projectXmlPath, newFilesMap);
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
                        FileMappingUtils.insertNewMapping(projectXmlPath, newFilesMap);
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
                        removedList.add(FileMappingUtils.path2String(delPath, itemType == FileNodeEntity.NODE_TYPE_FILE));
                        FileMappingUtils.removeMappingFromXml(projectXmlPath,
                                removedList,
                                JOptionPane.OK_OPTION == confirmRet);
                        ((DefaultTreeModel) jTree.getModel()).removeNodeFromParent(removedNode);
                    }
                    break;
                case "menuitem_close":
                    DefaultMutableTreeNode selectedProject = ((DefaultMutableTreeNode) jTree.getLastSelectedPathComponent());
                    confirmRet = JOptionPane.showConfirmDialog(null, "确认关闭工程" + selectedProject.toString() + "？");
                    if(confirmRet == JOptionPane.OK_OPTION) {
                        FileMappingUtils.closeProject(selectedProject.toString());
                        ((DefaultTreeModel) jTree.getModel()).removeNodeFromParent(selectedProject);
                    }
                    break;
                case "menuitem_newFolder":
                    DefaultMutableTreeNode selectedNode = ((DefaultMutableTreeNode) jTree.getLastSelectedPathComponent());
                    TreeNode[] selectedTreeNode = Arrays.copyOfRange(selectedNode.getPath(), 2, selectedNode.getPath().length);
                    String selectedPath = FileMappingUtils.path2String(selectedTreeNode, false);
                    String newDirName = (String)JOptionPane.showInputDialog(null,
                            "在" + selectedPath + "下创建文件夹：");
                    String selectedRealPath = ((FileNodeEntity) (selectedNode).getUserObject()).getRealPath();
                    File fullRealPath = new File(selectedRealPath + "/" + newDirName + "/");
                    if(!fullRealPath.mkdirs()) {
                        JOptionPane.showMessageDialog(null,
                                "创建文件夹失败",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        FileNodeEntity fileNodeEntity = new FileNodeEntity(newDirName, newDirName);
                        fileNodeEntity.setNodeType(FileNodeEntity.NODE_TYPE_DIR);
                        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(fileNodeEntity);
                        newNode.setAllowsChildren(true);

                        FileMappingUtils.insertNewMapping(projectXmlPath,
                                selectedPath + newDirName + "/",
                                fullRealPath.getPath() + "/");

                        ((DefaultTreeModel) jTree.getModel()).insertNodeInto(newNode,
                                selectedNode, selectedNode.getChildCount());
                    }
                    break;
            }
        });
    }

}
