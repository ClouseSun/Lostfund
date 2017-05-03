package cn.bit.ui.component;

import cn.bit.Context;
import cn.bit.exec.ExecUtils;
import cn.bit.exec.TestEntity;
import cn.bit.exec.TestMakefile;
import cn.bit.ui.ExecTableCellEditor;
import cn.bit.ui.ExecTreeCellRenderer;
import cn.bit.ui.frame.Main;
import org.apache.commons.io.IOUtils;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by KlousesSun on 2017/4/27.
 */
public class JXVersionTreeTable extends JXTreeTable {
    public JXVersionTreeTable(DefaultTreeTableModel defaultTreeTableModel, Main mainFrame) {

        super(defaultTreeTableModel);
        setRowSelectionAllowed(true);
        setTreeCellRenderer(new ExecTreeCellRenderer());
        setDefaultEditor(TestEntity.class, new ExecTableCellEditor(new JComboBox()));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    try {
                        int selectedRow = getRowForPath(getPathForLocation(e.getX(), e.getY()));
                        TestEntity selectedNode = (TestEntity) getValueAt(selectedRow, 1);
                        if (selectedNode != null) {
                            TestMakefile prjMakefile = Context.getContext().getActiveProject().getMakefile();
                            Process execProcess = null;
                            switch (selectedNode.getTestName()) {
                                case "规则检查":
                                    execProcess = prjMakefile.execCc("ver_" + String.valueOf(mainFrame.getVersionTabbedPane().getSelectedIndex()));
                                    break;
                                case "跨时钟域检查":
                                    execProcess = prjMakefile.execCdc("ver_" + String.valueOf(mainFrame.getVersionTabbedPane().getSelectedIndex()));
                                    break;
                                case "静态时序分析":
                                    execProcess = prjMakefile.execSta();
                                    break;
                                case "功能仿真":
                                    execProcess = prjMakefile.execSim("ver_" + String.valueOf(mainFrame.getVersionTabbedPane().getSelectedIndex()),
                                            selectedNode.getSelectedCase());
                                    break;
                                case "回归仿真":
                                    execProcess = prjMakefile.execSimRgs("ver_" + String.valueOf(mainFrame.getVersionTabbedPane().getSelectedIndex()));
                                    break;
                                case "波形调试":
                                    execProcess = prjMakefile.execVerdi("ver_" + String.valueOf(mainFrame.getVersionTabbedPane().getSelectedIndex()),
                                            selectedNode.getSelectedCase());
                                    break;
                                case "testC":
                                        execProcess = prjMakefile.execBuild("/Users/KlousesSun/ITEtest/test.c",
                                                "/Users/KlousesSun/ITEtest/test");
                                    break;
                            }
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
                            //mainFrame.getConsoleErrorArea().setText(IOUtils.toString(execProcess.getErrorStream()));
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }
}
