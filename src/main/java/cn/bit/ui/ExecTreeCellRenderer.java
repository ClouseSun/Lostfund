package cn.bit.ui;

import cn.bit.Context;
import cn.bit.exec.ExecMutableTreeTableNode;
import org.jdesktop.swingx.tree.DefaultXTreeCellRenderer;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

import static cn.bit.exec.TestEntity.ENTRY_TYPE_CLASS;

/**
 * Created by KlousesSun on 2017/4/13.
 */
public class ExecTreeCellRenderer extends DefaultTreeCellRenderer {

    public ExecTreeCellRenderer() {
        super();
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        ExecMutableTreeTableNode node = ((ExecMutableTreeTableNode) value);
        setText(node.getTestEntity().getTestName());

        if (node.getTestEntity().getEntryType() == ENTRY_TYPE_CLASS) {
            this.setIcon(new ImageIcon(Context.RES_ROOT + "icons/icon_testClass.png"));
        } else {
            switch (node.getTestEntity().getTestStatus()) {
                case PASSED:
                    this.setIcon(new ImageIcon(Context.RES_ROOT + "icons/icon_testPassed.png"));
                    break;
                case FAILED:
                    this.setIcon(new ImageIcon(Context.RES_ROOT + "icons/icon_testFailed.png"));
                    break;
                case RUNNING:
                    this.setIcon(new ImageIcon(Context.RES_ROOT + "icons/icon_testRunning.png"));
                    break;
            }
        }
        return this;
    }
}