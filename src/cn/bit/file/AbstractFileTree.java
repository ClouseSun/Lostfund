package cn.bit.file;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/**
 * Created by zhehua on 21/03/2017.
 */
public class AbstractFileTree {

    DefaultTreeModel fileTreeModel;

    public AbstractFileTree(DefaultTreeModel fileTreeModel) {
        this.fileTreeModel = fileTreeModel;
    }
}
