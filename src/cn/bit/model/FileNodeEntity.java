package cn.bit.model;

/**
 * Entity class defining a tree node model in project tree.
 * Each contains abstract file/folder name and corresponding real name.
 * Besides, {@link #nodeType} defines its type as directory/file/root(project name).
 *
 * The {@link #abstractName} doesn't contain the full path in the project tree but the name of the node.
 * But the {@link #realName} is full path on machine.
 * For example, if a node's abstract path in project is "a/b/c", and the corresponding real path is "/home/user/hello",
 * The {@link #abstractName} is "c", and {@link #realName} is "/home/user/hello", as shown below:
 *
 *      a
 *      |_b
 *      | |_c {abstractName = c, realName = /home/user/hello}
 *      | |_...
 *      |_...
 *
 * If the middle nodes on the path(node "a" and "b") have no corresponding real path, the variable {@link #realName}
 * would be null.
 */
public class FileNodeEntity {
    public static final int NODE_TYPE_DIR = 0;
    public static final int NODE_TYPE_FILE = 1;
    public static final int NODE_TYPE_ROOT = 2;

    private String abstractName;
    private String realName;
    private int nodeType = NODE_TYPE_DIR;

    /**
     * Construct entity object with abstract file/folder name and its real name.
     * Take attention that the node type is not specified.
     */
    public FileNodeEntity(String abstractName, String realName) {
        this.abstractName = abstractName;
        this.realName = realName;
    }

    /**
     * Construct entity object abstract file/folder name, real name
     * and its note type of dir/file/root(project name).
     */
    public FileNodeEntity(String abstractName, String realName, int nodeType) {
        this(abstractName, realName);
        this.nodeType = nodeType;
    }

    /**
     * Construct entity object with node type of dir/file/root(project name).
     * Take attention that the abstract and real file/dir name are null by default.
     */
    public FileNodeEntity(int nodeType) {
        this.nodeType = nodeType;
    }

    public String getAbstractName() {
        return abstractName;
    }

    public void setAbstractName(String abstractName) {
        this.abstractName = abstractName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getNodeType() {
        return nodeType;
    }

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof FileNodeEntity)
            return abstractName.equals(((FileNodeEntity) obj).getAbstractName());
        else if (obj instanceof String)
            return abstractName.equals(obj);
        return false;
    }

    @Override
    public String toString() {
        return abstractName;
    }
}
