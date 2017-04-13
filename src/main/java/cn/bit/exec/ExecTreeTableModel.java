package cn.bit.exec;

import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

import java.util.List;

/**
 * Created by KlousesSun on 2017/4/13.
 */
public class ExecTreeTableModel extends DefaultTreeTableModel {
    @Override
    public boolean isCellEditable(Object node, int column) {
        if(node == null || node.toString().equals("null")) {
            return false;
        }
        return (column == 1
                && ((ExecMutableTreeTableNode) node).getTestEntity().getTestCases() != null
                && !((ExecMutableTreeTableNode) node).getTestEntity().getTestCases().isEmpty());
    }

    @Override
    public Class<?> getColumnClass(int column) {
        switch (column){
            case 0:
                return String.class;
            case 1:
                return TestEntity.class;
        }
        return null;
    }

    @Override
    public void setValueAt(Object value, Object node, int column) {
        switch (column) {
            case 0:
                ((ExecMutableTreeTableNode) node).testEntity.testName = value.toString();
                break;
            case 1:
                ((ExecMutableTreeTableNode) node).testEntity.selectedCase = value.toString();
                break;
        }
    }
}
