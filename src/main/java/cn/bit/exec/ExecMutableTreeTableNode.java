package cn.bit.exec;

import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;

/**
 * Created by zhehua on 11/04/2017.
 */
public class ExecMutableTreeTableNode extends AbstractMutableTreeTableNode {
    TestEntity testEntity;

    public ExecMutableTreeTableNode(TestEntity testEntity) {
        this.testEntity = testEntity;
    }

    @Override
    public Object getValueAt(int column) {
        switch (column) {
            case 0:
                return testEntity.testName;
            case 1:
                return testEntity.options;
            default:
                return null;
        }
    }

    @Override
    public int getColumnCount() {
        return testEntity.entryType;
    }
}
