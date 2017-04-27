package cn.bit.ui.component;

import cn.bit.Context;
import cn.bit.file.FileMappingUtils;
import cn.bit.model.FileNodeEntity;
import cn.bit.model.MenuList;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.dom4j.DocumentException;

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
                jsonPopMenuPath = Context.getContext().getJsonDirPopMenuPath();
                break;
            case FileNodeEntity.NODE_TYPE_FILE:
                jsonPopMenuPath = Context.getContext().getJsonFilePopMenuPath();
                break;
            case FileNodeEntity.NODE_TYPE_ROOT:
                jsonPopMenuPath = Context.getContext().getJsonProjectPopMenuPath();
                break;
            default:
                jsonPopMenuPath = Context.getContext().getJsonFilePopMenuPath();
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
            TreeNode[] newPath = Arrays.copyOfRange(parentNode.getPath(), 2, parentNode.getPath().length);
            String abstractPath = FileMappingUtils.path2String(newPath, false)
                    + newFile.getName() + "/";
            newFilesMap.put(abstractPath, newFile.getPath() + "/");

            String dirPathToInsert = ((FileNodeEntity) (parentNode).getUserObject()).getRealPath();
            FileNodeEntity fileNodeEntity = new FileNodeEntity(newFile.getName(),
                    dirPathToInsert + newFile.getName() + "/");
            fileNodeEntity.setNodeType(FileNodeEntity.NODE_TYPE_DIR);
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(fileNodeEntity);
            newNode.setAllowsChildren(true);
            treeModel.insertNodeInto(newNode, parentNode, parentNode.getChildCount());

            File[] childFileList = newFile.listFiles();
            for(int i = 0; i < childFileList.length; i++) {
                addFile(childFileList[i], newFilesMap, treeModel, newNode);
            }
        } else {
            String dirPathToInsert = ((FileNodeEntity) (parentNode).getUserObject()).getRealPath();
            FileNodeEntity fileNodeEntity = new FileNodeEntity(newFile.getName(),
                    dirPathToInsert + newFile.getName());
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
            String dirPathToInsert = ((FileNodeEntity) (parentNode).getUserObject()).getRealPath();
            FileNodeEntity fileNodeEntity = new FileNodeEntity(sourceFile.getName(),
                    dirPathToInsert + sourceFile.getName() + "/");
            fileNodeEntity.setNodeType(FileNodeEntity.NODE_TYPE_DIR);
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(fileNodeEntity);
            newNode.setAllowsChildren(true);
            treeModel.insertNodeInto(newNode, parentNode, parentNode.getChildCount());

            TreeNode[] newPath = Arrays.copyOfRange(parentNode.getPath(), 2, parentNode.getPath().length);

            String abstractPath = FileMappingUtils.path2String(newPath, false)
                    + sourceFile.getName() + "/";

            newFilesMap.put(abstractPath, dirPathToInsert + sourceFile.getName() + "/");
            File newFile = new File(dirPathToInsert + sourceFile.getName());
            newFile.mkdir();
            File[] childFileList = sourceFile.listFiles();
            for(int i = 0; i < childFileList.length; i++) {
                addCopyFile(childFileList[i], newFilesMap, treeModel, newNode);
            }
        } else {
                try {
                String dirPathToInsert = ((FileNodeEntity) (parentNode).getUserObject()).getRealPath();
                FileNodeEntity fileNodeEntity = new FileNodeEntity(sourceFile.getName(),
                        dirPathToInsert + sourceFile.getName());
                fileNodeEntity.setNodeType(FileNodeEntity.NODE_TYPE_FILE);
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(fileNodeEntity);
                newNode.setAllowsChildren(false);
                treeModel.insertNodeInto(newNode, parentNode, parentNode.getChildCount());
                TreeNode[] newPath = Arrays.copyOfRange(parentNode.getPath(), 2, parentNode.getPath().length);
                String abstractPath = FileMappingUtils.path2String(newPath, false)
                        + sourceFile.getName();
                newFilesMap.put(abstractPath, dirPathToInsert + sourceFile.getName());
                File newFile = new File(dirPathToInsert + sourceFile.getName());
                IOUtils.copy(new FileInputStream(sourceFile), new FileOutputStream(newFile));

                } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    private void bindMenuItemListener(JMenuItem jMenuItem, JTree jTree) {
        jMenuItem.addActionListener((ActionEvent e) -> {
            DefaultMutableTreeNode selectedNode = ((DefaultMutableTreeNode) jTree.getLastSelectedPathComponent());
            String parentPath = ((FileNodeEntity) selectedNode.getUserObject()).getRealPath();
            String projectName = selectedNode.getPath()[1].toString();
            String projectXmlPath = Context.getContext().getProjectFilePath(projectName);
            int nodeType = ((FileNodeEntity) selectedNode.getUserObject()).getNodeType();
            TreeNode[] path = Arrays.copyOfRange(selectedNode.getPath(), 2, selectedNode.getPath().length);
            String selectedPath = FileMappingUtils.path2String(path, nodeType == FileNodeEntity.NODE_TYPE_FILE);


            JFileChooser jFileChooser = new JFileChooser(projectXmlPath.substring(0, projectXmlPath.lastIndexOf("/")));


            switch (jMenuItem.getName()) {
                case "menuitem_addExisting":
                    jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    jFileChooser.setMultiSelectionEnabled(true);

                    if (jFileChooser.showOpenDialog(null) != JFileChooser.CANCEL_OPTION) {
                        Map<String, String> newFilesMap = new HashMap<>();
                        Arrays.stream(jFileChooser.getSelectedFiles()).forEach(newFile ->
                                addFile(newFile,
                                        newFilesMap,
                                        ((DefaultTreeModel) jTree.getModel()),
                                        ((DefaultMutableTreeNode) jTree.getLastSelectedPathComponent())));
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

                        Arrays.stream(jFileChooser.getSelectedFiles()).forEach(sourceFile ->
                                addCopyFile(sourceFile,
                                        newFilesMap,
                                        ((DefaultTreeModel) jTree.getModel()),
                                        ((DefaultMutableTreeNode) jTree.getLastSelectedPathComponent())));

                        FileMappingUtils.insertNewMapping(projectXmlPath, newFilesMap);
                    } else {
                        return;
                    }
                    break;
                case "menuitem_delete":
                    int confirmRet = JOptionPane.showConfirmDialog(null, "是否删除真实目录下的文件？");

                    if (confirmRet != JOptionPane.CANCEL_OPTION) {
                        List<String> removedList = new LinkedList<>();
                        TreeNode[] delPath = Arrays.copyOfRange(selectedNode.getPath(),
                                2,
                                selectedNode.getPath().length);

                        removedList.add(FileMappingUtils.path2String(delPath,
                                nodeType == FileNodeEntity.NODE_TYPE_FILE));

                        FileMappingUtils.removeMappingFromXml(projectXmlPath,
                                removedList,
                                JOptionPane.OK_OPTION == confirmRet);

                        ((DefaultTreeModel) jTree.getModel()).removeNodeFromParent(selectedNode);
                    }
                    break;
                case "menuitem_close":
                    confirmRet = JOptionPane.showConfirmDialog(null,
                            "确认关闭工程" + selectedNode.toString() + "?");

                    if(confirmRet == JOptionPane.OK_OPTION) {
                        FileMappingUtils.closeProject(selectedNode.toString());
                        ((DefaultTreeModel) jTree.getModel()).removeNodeFromParent(selectedNode);
                    }
                    break;
                case "menuitem_newFolder":
                    String newDirName = JOptionPane.showInputDialog(null,
                            "在" + selectedPath + "下创建文件夹：");

                    if(newDirName != null && !newDirName.equals("")) {
                        String selectedRealPath = ((FileNodeEntity) (selectedNode).getUserObject()).getRealPath();
                        File fullRealPath = new File(selectedRealPath + "/" + newDirName + "/");
                        if (!fullRealPath.mkdirs()) {
                            JOptionPane.showMessageDialog(null,
                                    "创建文件夹失败",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            FileNodeEntity fileNodeEntity = new FileNodeEntity(newDirName,
                                    selectedRealPath + "/" + newDirName + "/");
                            fileNodeEntity.setNodeType(FileNodeEntity.NODE_TYPE_DIR);
                            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(fileNodeEntity);
                            newNode.setAllowsChildren(true);

                            FileMappingUtils.insertNewMapping(projectXmlPath,
                                    selectedPath + newDirName + "/",
                                    fullRealPath.getPath() + "/");

                            ((DefaultTreeModel) jTree.getModel()).insertNodeInto(newNode,
                                    selectedNode, selectedNode.getChildCount());
                        }
                    }
                    break;
                case "menuitem_markAsTesting":
                    FileMappingUtils.setActivatedProject(selectedNode.toString());
                    Context.getContext().setActiveProject(Context.getContext().getOpenProjects().get(selectedNode.toString()));
                    break;
                case "menuitem_rename":
                    String oldNameString = ((FileNodeEntity) selectedNode.getUserObject()).getAbstractName();
                    String newNameString = JOptionPane.showInputDialog(null,
                            "将 " + oldNameString + " 更名为：");
                    if(newNameString != null && !newNameString.equals("")) {
                        FileMappingUtils.renameFile(newNameString, selectedPath, projectXmlPath, true);
                    }
                    ((FileNodeEntity) selectedNode.getUserObject()).setAbstractName(newNameString);
                    break;
                case "menuitem_newVersion":
                    try {
                        int newVerIndex = Context.getContext().getProjectByName(projectName).getExecModels().size();
                        Context.getContext().getProjectByName(projectName).newVersion(new FileInputStream(Context.defaultPrjXmlPath));
                        Context.
                                getContext().
                                getProjectByName(projectName).
                                getProjectTree().
                                addAll(FileMappingUtils.addNewVerToXml(projectXmlPath, newVerIndex));

                    } catch (IOException | DocumentException e1) {
                        e1.printStackTrace();
                    }
                    break;
            }
        });
    }

}
