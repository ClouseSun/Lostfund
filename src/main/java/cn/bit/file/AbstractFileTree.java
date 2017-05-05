package cn.bit.file;

import cn.bit.model.FileNodeEntity;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This "tree" is a holder of projects' root to conveniently build it by abstract/real path mappings.
 */
public class AbstractFileTree {

    private DefaultMutableTreeNode projectTreeRoot;

    public AbstractFileTree(DefaultMutableTreeNode projectTreeRoot) {
        this.projectTreeRoot = projectTreeRoot;
    }

    public AbstractFileTree(String projectName, String projectPath) {
        FileNodeEntity entity = new FileNodeEntity(FileNodeEntity.NODE_TYPE_ROOT);
        entity.setAbstractName(projectName);
        entity.setRealPath(projectPath);
        projectTreeRoot = new DefaultMutableTreeNode(entity);
        projectTreeRoot.setAllowsChildren(true);
    }

    protected DefaultMutableTreeNode getNodeByPath(TreePath path) {
        DefaultMutableTreeNode currentTreeNode = projectTreeRoot;
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
                currentTreeNode.setAllowsChildren(true);
                currentTreeNode.add(newNode);
                currentTreeNode = newNode;
            }
        }
        return currentTreeNode;
    }

    protected List<String> getPathByString(String abstractPath) {
        abstractPath += '#';
        int nodeType = FileNodeEntity.NODE_TYPE_FILE;
        List<String> pathList = new LinkedList<>();
        StringBuilder dirName = new StringBuilder();
        for (int i = 0;i < abstractPath.length(); i ++) {
            switch(abstractPath.charAt(i)) {
                case '#':
                    if (dirName.length() == 0) {
                        nodeType = FileNodeEntity.NODE_TYPE_DIR;
                    }
                case '/':
                    if (dirName.length() > 0) {
                        pathList.add(dirName.toString());
                        dirName.setLength(0);
                        break;
                    } else {
                        continue;
                    }
                default:
                    dirName.append(abstractPath.charAt(i));
                    break;
            }
        }
        // String node type as the last element
        if (nodeType == FileNodeEntity.NODE_TYPE_DIR)
            pathList.add("#DIR#");
        else
            pathList.add("#FILE#");
        return pathList;
    }

    protected void buildPath(TreePath path, Object newValue) {
        DefaultMutableTreeNode currentTreeNode = getNodeByPath(path);
        currentTreeNode.setAllowsChildren(((FileNodeEntity) newValue).getNodeType() != FileNodeEntity.NODE_TYPE_FILE);
        currentTreeNode.setUserObject(newValue);
    }

    /**
     * Add child given abstract/real path as Strings.
     */
    public void addChild(String abstractPath, String realPath) {

            int nodeType = FileNodeEntity.NODE_TYPE_FILE;

            List<String> pathList = getPathByString(abstractPath);
            String nodeTypeString = pathList.remove(pathList.size() - 1);
            TreePath treePath = new TreePath(pathList.toArray());

            FileNodeEntity entity = new FileNodeEntity(pathList.get(pathList.size() - 1), realPath);
            if (nodeTypeString.equals("#DIR#")) {
                nodeType = FileNodeEntity.NODE_TYPE_DIR;
            }
            entity.setNodeType(nodeType);

            buildPath(treePath, entity);
    }

    /**
     * Add all abstract/real path to tree model.
     * @param fileMap Key-value pair representing abstract/real.
     */
    public void addAll(Map<String, String> fileMap) {
        for (Map.Entry<String, String> entry : fileMap.entrySet()) {
            addChild(entry.getKey(), entry.getValue());
        }
    }

    public void rebuildAll(Map<String, String> fileMap) {
        projectTreeRoot.removeAllChildren();
        addAll(fileMap);
    }

    public DefaultMutableTreeNode getProjectTreeRoot() {
        return projectTreeRoot;
    }
}
