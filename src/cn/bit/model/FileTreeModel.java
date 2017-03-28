package cn.bit.model;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.Enumeration;

/**
 * Tree model for JTree built for project tree UI.
 * In the tree model, each node as {@link DefaultMutableTreeNode} represents a UI node of project tree.
 * And necessary information for a UI node stores in the {@link DefaultMutableTreeNode#userObject}
 * as {@link FileNodeEntity} including its abstract file/folder name, real name and type(dir/file/project root).
 *
 * @see FileNodeEntity
 */
public class FileTreeModel extends DefaultTreeModel {
    public FileTreeModel(TreeNode root) {
        super(root);
    }

    public FileTreeModel(TreeNode root, boolean asksAllowsChildren) {
        super(root, asksAllowsChildren);
    }

    /**
     * Replace the user object by newValue if the node identified by path if exists.
     * If not, the new nodes traced in path would be created, and then do the replacement.
     * @param path identify the affected node
     * @param newValue the new user object set to the TreeNode identified by path.
     */
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        DefaultMutableTreeNode currentTreeNode = (DefaultMutableTreeNode) this.getRoot();
        for (Object nodeName : path.getPath()) {
            Enumeration children = currentTreeNode.children();
            boolean isFoundChild = false;
            while(children.hasMoreElements()) {
                DefaultMutableTreeNode aChildNode = (DefaultMutableTreeNode)children.nextElement();
                if((aChildNode.getUserObject()).equals(nodeName)) {
                    isFoundChild = true;
                    currentTreeNode = aChildNode;
                    break;
                }
            }
            if (!isFoundChild) {
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new FileNodeEntity((String)nodeName, null));
                currentTreeNode.add(newNode);
                currentTreeNode = newNode;
            }
        }
        currentTreeNode.setUserObject(newValue);
        nodeChanged(currentTreeNode);
    }
}
