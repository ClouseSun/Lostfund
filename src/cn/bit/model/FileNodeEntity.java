package cn.bit.model;

/**
 * Created by zhehua on 22/03/2017.
 */
public class FileNodeEntity {
    public static final int NODE_TYPE_DIR = 0;
    public static final int NODE_TYPE_FILE = 1;
    public static final int NODE_TYPE_ROOT = 2;

    private String abstractFileName;
    private String realFileName;
    private int nodeType = NODE_TYPE_DIR;


    public FileNodeEntity(String abstractFileName, String realFileName) {
        this.abstractFileName = abstractFileName;
        this.realFileName = realFileName;
    }

    public FileNodeEntity(int nodeType) {
        this.nodeType = nodeType;
    }

    public String getAbstractFileName() {
        return abstractFileName;
    }

    public void setAbstractFileName(String abstractFileName) {
        this.abstractFileName = abstractFileName;
    }

    public String getRealFileName() {
        return realFileName;
    }

    public void setRealFileName(String realFileName) {
        this.realFileName = realFileName;
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
            return abstractFileName.equals(((FileNodeEntity) obj).getAbstractFileName());
        else if (obj instanceof String)
            return abstractFileName.equals(obj);
        return false;
    }

    @Override
    public String toString() {
        return abstractFileName;
    }
}
