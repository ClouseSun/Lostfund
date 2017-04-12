package cn.bit.ui.frame;

import cn.bit.Context;
import cn.bit.model.FileNodeEntity;
import cn.bit.ui.FileTreeCellRender;
import cn.bit.ui.component.JsonMenuBar;
import cn.bit.ui.component.JsonTreePopMenu;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.apache.commons.io.IOUtils;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by zhehua on 19/03/2017.
 */
public class Main {

    private JPanel mainPanel;
    private JScrollPane projectFilePane;
    private JPanel projectViewPane;

    private JTree projectTree;
    private JSplitPane consoleSplitPane;
    private JSplitPane editorSplitPane;
    private JTabbedPane consolePane;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JTextArea textArea3;
    private JTextArea textArea4;
    private JScrollPane testPane;
    private JTabbedPane execTabbedPane;
    private JPanel ver1Pane;
    private JPanel ver2Pane;
    private JXTreeTable jxTreeTable;

    private JFrame mainFrame;

    public static void main(String[] args) {
        new Main();
    }


    public Main() {
        mainFrame = new JFrame();
        mainFrame.setContentPane(mainPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        mainFrame.setSize((int) screenSize.getWidth(), (int) screenSize.getWidth());

        JMenuBar menuBar = null;

        Context.init(Context.configureFilePath);

        try {
            String jsonMenuBarString = IOUtils.toString(new FileInputStream(Context.getContext().getJsonMenuBarPath()));
            menuBar = new JsonMenuBar(jsonMenuBarString, projectTree);
            mainFrame.setJMenuBar(menuBar);
        } catch (IOException e) {
            e.printStackTrace();
        }

        projectTree.setModel(Context.getContext().getProjectFileModel());
        projectTree.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                TreePath treePath = projectTree.getPathForLocation(e.getX(), e.getY());
                if (treePath == null) {
                    return;
                }
                projectTree.setSelectionPath(treePath);

                if (e.getButton() == MouseEvent.BUTTON3) {
                    new JsonTreePopMenu(projectTree).show(projectTree, e.getX(), e.getY());
                } else if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    FileNodeEntity selectedNode = ((FileNodeEntity) ((DefaultMutableTreeNode) projectTree.
                            getLastSelectedPathComponent()).
                            getUserObject());
                    if (selectedNode.getNodeType() == FileNodeEntity.NODE_TYPE_FILE) {
                        return;
                    }

                    switch (System.getProperty("os.name").toString()) {
                        case "Mac OS X":
                            try {
                                if (selectedNode.getNodeType() == FileNodeEntity.NODE_TYPE_DIR)
                                    Runtime.getRuntime().exec("open " + selectedNode.getRealPath());
                                if (selectedNode.getNodeType() == FileNodeEntity.NODE_TYPE_ROOT)
                                    Runtime.getRuntime().exec("open "
                                            + selectedNode.getRealPath().substring(0,
                                            selectedNode.getRealPath().lastIndexOf('/')));
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            break;
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        projectTree.setCellRenderer(new FileTreeCellRender());

        jxTreeTable = new JXTreeTable(Context.getContext().getActiveProject().getExecModels().get("ver_0"));
        ver1Pane.add(jxTreeTable);

        mainFrame.setVisible(true);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                consoleSplitPane.setDividerLocation(0.5);
                editorSplitPane.setDividerLocation(0.5);
            }
        });
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        consoleSplitPane = new JSplitPane();
        consoleSplitPane.setContinuousLayout(false);
        consoleSplitPane.setDividerLocation(200);
        consoleSplitPane.setDividerSize(3);
        consoleSplitPane.setDoubleBuffered(false);
        consoleSplitPane.setInheritsPopupMenu(true);
        consoleSplitPane.setOneTouchExpandable(true);
        consoleSplitPane.setOrientation(0);
        consoleSplitPane.setResizeWeight(0.0);
        mainPanel.add(consoleSplitPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        editorSplitPane = new JSplitPane();
        editorSplitPane.setContinuousLayout(true);
        editorSplitPane.setDividerLocation(200);
        editorSplitPane.setDividerSize(3);
        editorSplitPane.setEnabled(true);
        consoleSplitPane.setLeftComponent(editorSplitPane);
        projectViewPane = new JPanel();
        projectViewPane.setLayout(new GridBagLayout());
        editorSplitPane.setLeftComponent(projectViewPane);
        projectFilePane = new JScrollPane();
        projectFilePane.setPreferredSize(new Dimension(-1, -1));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        projectViewPane.add(projectFilePane, gbc);
        projectTree = new JTree();
        projectTree.setRootVisible(false);
        projectTree.setShowsRootHandles(true);
        projectTree.putClientProperty("JTree.lineStyle", "");
        projectFilePane.setViewportView(projectTree);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        projectViewPane.add(scrollPane1, gbc);
        testPane = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        projectViewPane.add(testPane, gbc);
        execTabbedPane = new JTabbedPane();
        testPane.setViewportView(execTabbedPane);
        ver1Pane = new JPanel();
        ver1Pane.setLayout(new GridBagLayout());
        execTabbedPane.addTab("ver1", ver1Pane);
        ver2Pane = new JPanel();
        ver2Pane.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        execTabbedPane.addTab("ver2", ver2Pane);
        final JScrollPane scrollPane2 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        projectViewPane.add(scrollPane2, gbc);
        consolePane = new JTabbedPane();
        consolePane.setEnabled(true);
        consoleSplitPane.setRightComponent(consolePane);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        consolePane.addTab("系统控制台", panel1);
        textArea1 = new JTextArea();
        textArea1.setEnabled(false);
        panel1.add(textArea1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        consolePane.addTab("系统控制台错误", panel2);
        textArea2 = new JTextArea();
        textArea2.setEnabled(false);
        panel2.add(textArea2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        consolePane.addTab("系统控制台警告", panel3);
        textArea3 = new JTextArea();
        textArea3.setEnabled(false);
        panel3.add(textArea3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        consolePane.addTab("测试软件EDA控制台", panel4);
        textArea4 = new JTextArea();
        textArea4.setEnabled(false);
        panel4.add(textArea4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
