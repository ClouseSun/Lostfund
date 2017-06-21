package cn.bit.ui.component;

import cn.bit.Context;
import cn.bit.exec.ExecUtils;
import cn.bit.exec.TestEntity;
import cn.bit.exec.TestMakefile;
import cn.bit.file.FileMappingUtils;
import cn.bit.ui.ExecTableCellEditor;
import cn.bit.ui.ExecTreeCellRenderer;
import cn.bit.ui.frame.Main;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTableUI;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by KlousesSun on 2017/4/27.
 */
public class JXVersionTreeTable extends JXTreeTable {
    @Override
    public void updateUI() {
        super.updateUI();
        setUI(new MyTableUI());
    }

    public JXVersionTreeTable(DefaultTreeTableModel defaultTreeTableModel, Main mainFrame) {

        super(defaultTreeTableModel);
        setRowSelectionAllowed(true);
        setTreeCellRenderer(new ExecTreeCellRenderer());
        setDefaultEditor(TestEntity.class, new ExecTableCellEditor(new JTextField()));



        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    try {
                        int selectedRow = getRowForPath(getPathForLocation(e.getX(), e.getY()));
                        TestEntity selectedNode = (TestEntity) getValueAt(selectedRow, 1);
                        if (selectedNode != null) {
                            TestMakefile prjMakefile = Context.getContext().getActiveProject().getMakefile();
                            Process execProcess = prjMakefile.exec(selectedNode.getTestArg());
                            mainFrame.getEdaMsgArea().setText(IOUtils.toString(execProcess.getInputStream()));
                            BufferedReader br = new BufferedReader(new InputStreamReader(execProcess.getErrorStream()));
                            String line;
                            while(null != (line = br.readLine())) {
                                switch (ExecUtils.classifyInputStringFromMakefile(line)) {
                                    case ExecUtils.NORMAL_LINE:
                                        mainFrame.getEdaMsgArea().append(line + "\n");
                                        break;
                                    case ExecUtils.WARNING_LINE:
                                        mainFrame.getEdaWarningArea().append(line + "\n");
                                        break;
                                    case ExecUtils.ERROR_LINE:
                                        mainFrame.getEdaErrorArea().append(line + "\n");
                                        break;
                                }
                            }
                            Document document = new SAXReader().read(new FileInputStream(Context.getContext().getActiveProject().getProjectConfigPath()));
                            Context.getContext().getActiveProject().getLogTree().rebuildAll(FileMappingUtils.loadFileMapping(
                                    document.getRootElement().element("userLogMapping"), true));
                            Context.getContext().getLogFileModel().reload(Context.getContext().getActiveProject().getLogTree().getProjectTreeRoot());

                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (DocumentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    class MyTableUI extends BasicTableUI {
        @Override
        protected void installKeyboardActions() {

        }
    }
}
