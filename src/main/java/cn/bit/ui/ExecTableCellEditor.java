package cn.bit.ui;

import cn.bit.exec.TestEntity;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Locale;

/**
 * Created by KlousesSun on 2017/4/13.
 */
public class ExecTableCellEditor extends DefaultCellEditor {

    public ExecTableCellEditor(JTextField jTextField) {
        super(jTextField);

        ArrayList<String> items = new ArrayList<String>();
        items.add("tc_uart_001");
        items.add("tc_uart_002");
        items.add("tc_uart_003");
        items.add("tc_uart_004");
        items.add("tc_uart_005");
        setupAutoComplete(jTextField, items);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value == null) return null;
        TestEntity node = ((TestEntity) value);
        JTextField jTextField = ((JTextField) this.getComponent());


       // jTextField.setText(node.getSelectedCase());

//        JComboBox jComboBox = ((JComboBox) this.getComponent());
//        jComboBox.removeAllItems();
//
//        for (String option : node.getTestCases()) {
//            jComboBox.addItem(option);
//            if(option.equals(node.getSelectedCase())) {
//                jComboBox.setSelectedIndex(jComboBox.getItemCount() - 1);
//            }
//        }

        return this.getComponent();
    }

    private boolean isAdjusting(JComboBox cbInput) {
        if(cbInput.getClientProperty("is_adjusting") instanceof Boolean) {
            return ((Boolean) cbInput.getClientProperty("is_adjusting"));
        }
        return false;
    }

    private void setAdjusting(JComboBox cbInput, boolean adjusting) {
        cbInput.putClientProperty("is_adjusting", adjusting);
    }

    public void setupAutoComplete(final JTextField txtInput, final ArrayList<String> items) {
        final DefaultComboBoxModel model = new DefaultComboBoxModel();
        final JComboBox cbInput = new JComboBox(model) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, 0);
            }
        };
        setAdjusting(cbInput, false);
        for (String item : items) {
            model.addElement(item);
        }
        cbInput.setPopupVisible(false);
        cbInput.setSelectedItem(null);
        cbInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!isAdjusting(cbInput)) {
                    if(cbInput.getSelectedItem() != null) {
                        txtInput.setText(cbInput.getSelectedItem().toString());
                    }
                }
            }
        });

        txtInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                setAdjusting(cbInput, true);
                if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if(cbInput.isPopupVisible()) {
                        e.setKeyCode(KeyEvent.VK_ENTER);
                    }
                }
                if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    e.setSource(cbInput);
                    cbInput.dispatchEvent(e);
                    if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                        txtInput.setText(cbInput.getSelectedItem().toString());
                        cbInput.setPopupVisible(false);
                    }
                }
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    cbInput.setPopupVisible(false);
                }
                if(e.getKeyCode() == KeyEvent.VK_TAB && model.getSize() > 0) {
                    e.setSource(cbInput);

                    cbInput.setPopupVisible(true);
                }
                setAdjusting(cbInput, false);
            }
        });

        txtInput.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateList();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateList();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateList();
            }

            private  void updateList() {
                setAdjusting(cbInput, true);
                model.removeAllElements();
                String input = txtInput.getText();
                if(!input.isEmpty()) {
                    for(String item : items) {
                        if(item.toLowerCase().startsWith(input.toLowerCase())) {
                            model.addElement(item);
                        }
                    }
                }
                setAdjusting(cbInput, false);
            }
        });
        txtInput.setLayout(new BorderLayout());
        cbInput.setBorder(new EmptyBorder(40, 0, 0, 0));
        txtInput.add(cbInput, BorderLayout.SOUTH);
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }
}
