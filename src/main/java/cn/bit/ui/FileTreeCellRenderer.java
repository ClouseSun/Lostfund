package cn.bit.ui;

import cn.bit.Context;
import cn.bit.model.FileNodeEntity;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * Created by KlousesSun on 2017/4/12.
 */
public class FileTreeCellRenderer extends DefaultTreeCellRenderer {

    public FileTreeCellRenderer() {
        super();
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree,
                                                  Object value,
                                                  boolean sel,
                                                  boolean expanded,
                                                  boolean leaf,
                                                  int row,
                                                  boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        FileNodeEntity nodeEntity = ((FileNodeEntity) ((DefaultMutableTreeNode) value).getUserObject());
        int nodeType = nodeEntity.getNodeType();
        String activeProjectName = Context.getContext().getActiveProject().getProjectName();


        switch (nodeType) {
            case FileNodeEntity.NODE_TYPE_ROOT:
                if(nodeEntity.getAbstractName().equals(activeProjectName)) {
                    this.setIcon(new ImageIcon(Context.RES_ROOT + "icons/icon_activeProject.png"));
                } else {
                    this.setIcon(new ImageIcon(Context.RES_ROOT + "icons/icon_project.png"));
                }
                break;
            case FileNodeEntity.NODE_TYPE_DIR:
                this.setIcon(new ImageIcon(Context.RES_ROOT + "icons/icon_folder.png"));
                break;
            case FileNodeEntity.NODE_TYPE_FILE:
                this.setIcon(new ImageIcon(Context.RES_ROOT + "icons/icon_file.png"));
                break;
        }
        return this;
    }
}
