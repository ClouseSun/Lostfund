package cn.bit.ui;

import cn.bit.exec.TestEntity;

import javax.swing.*;
import java.awt.*;
import java.util.EventObject;

/**
 * Created by KlousesSun on 2017/4/13.
 */
public class ExecTableCellEditor extends DefaultCellEditor {

    public ExecTableCellEditor(JComboBox comboBox) {
        super(comboBox);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        if (value == null) return null;

        TestEntity node = ((TestEntity) value);
        JComboBox jComboBox = ((JComboBox) this.getComponent());
        jComboBox.removeAllItems();

        for (String option : node.getTestCases()) {
            jComboBox.addItem(option);
            if(option.equals(node.getSelectedCase())) {
                jComboBox.setSelectedIndex(jComboBox.getItemCount() - 1);
            }
        }
        return this.getComponent();
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }
}
