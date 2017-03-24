package cn.bit.file;

import cn.bit.model.FileNodeEntity;
import cn.bit.model.FileTreeModel;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhehua on 21/03/2017.
 */
public class AbstractFileTree {

    private FileTreeModel fileTreeModel;

    public AbstractFileTree(FileTreeModel fileTreeModel) {
        this();
        this.fileTreeModel = fileTreeModel;
    }

    public AbstractFileTree() {
        fileTreeModel = new FileTreeModel(
                new DefaultMutableTreeNode(new FileNodeEntity(FileNodeEntity.NODE_TYPE_ROOT)));
    }

    public void addChild(String abstractPath, String realPath, int nodeType) {
            abstractPath += '#';

            List<String> pathList = new LinkedList<>();
            StringBuilder dirName = new StringBuilder();
            for (int i = 0;i < abstractPath.length(); i ++) {
                switch(abstractPath.charAt(i)) {
                    case '/':
                    case '#':
                        pathList.add(dirName.toString());
                        dirName.setLength(0);
                        break;
                    default:
                        dirName.append(abstractPath.charAt(i));
                        break;
                }
            }
            TreePath treePath = new TreePath(pathList.toArray());

            FileNodeEntity entity = new FileNodeEntity(pathList.get(pathList.size() - 1), realPath);
            entity.setNodeType(nodeType);
            fileTreeModel.valueForPathChanged(treePath, entity);
    }

    public void addChild(String abstractPath, String realPath) {
        addChild(abstractPath, realPath, FileNodeEntity.NODE_TYPE_FILE);
    }

    public void addAll(Map<String, String> fileMap) {
       addAll(fileMap, FileNodeEntity.NODE_TYPE_FILE);
    }

    public void addAll(Map<String, String> fileMap, int nodeType) {
        for (Map.Entry<String, String> entry : fileMap.entrySet()) {
            addChild(entry.getKey(), entry.getValue(), nodeType);
        }
    }

    public FileTreeModel model() {
        return fileTreeModel;
    }
}
