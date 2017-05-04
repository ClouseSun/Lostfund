package cn.bit.file;

import cn.bit.model.FileNodeEntity;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.io.File;

/**
 * Created by zhehua on 04/05/2017.
 */
public class AbstractLogFileTree extends AbstractFileTree{

    public AbstractLogFileTree(DefaultMutableTreeNode projectTreeRoot) {
        super(projectTreeRoot);
    }

    public AbstractLogFileTree(String projectName, String projectPath) {
        super(projectName, projectPath);
    }

    protected void searchAndBuild(DefaultMutableTreeNode node, String realPath) {
        File pFile = new File(realPath);
        DefaultMutableTreeNode currentNode = node;
        if (pFile.isDirectory()) {
            for(File file : pFile.listFiles()) {
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new FileNodeEntity(file.getName(), file.getPath()));
                currentNode.add(newNode);
                searchAndBuild(newNode, file.getPath());
            }
        }
    }

    @Override
    protected void buildPath(TreePath path, Object newValue) {
        DefaultMutableTreeNode currentTreeNode = getNodeByPath(path);
        currentTreeNode.setAllowsChildren(((FileNodeEntity) newValue).getNodeType() != FileNodeEntity.NODE_TYPE_FILE);
        currentTreeNode.setUserObject(newValue);
        searchAndBuild(currentTreeNode, ((FileNodeEntity) newValue).getRealPath());
    }
}
