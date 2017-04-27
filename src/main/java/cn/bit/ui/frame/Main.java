package cn.bit.ui.frame;

import cn.bit.Context;
import cn.bit.model.FileNodeEntity;
import cn.bit.ui.FileTreeCellRenderer;
import cn.bit.ui.component.JXVersionTreeTable;
import cn.bit.ui.component.JsonMenuBar;
import cn.bit.ui.component.JsonTreePopMenu;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
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
    private JTabbedPane consoleTabbedPane;
    private JTextArea consoleMsgArea;
    private JTextArea consoleErrorArea;
    private JScrollPane testPane;
    private JTabbedPane versionTabbedPane;

    public static final int RUN_TAB_INDEX = 0;
    public static final int ERROR_TAB_INDEX = 1;

    private JFrame mainFrame;

    public static void main(String[] args) {
        new Main();
    }

    public JTree getProjectTree() {
        return projectTree;
    }

    public JTabbedPane getVersionTabbedPane() {
        return versionTabbedPane;
    }

    public JTextArea getConsoleMsgArea() {
        return consoleMsgArea;
    }

    public JTextArea getConsoleErrorArea() {
        return consoleErrorArea;
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
                    new JsonTreePopMenu(Main.this).show(projectTree, e.getX(), e.getY());
                } else if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {

                    FileNodeEntity selectedNode = ((FileNodeEntity) ((DefaultMutableTreeNode) projectTree.
                            getLastSelectedPathComponent()).
                            getUserObject());

                    if (selectedNode.getNodeType() == FileNodeEntity.NODE_TYPE_FILE) {
                        return;
                    }

                    switch (System.getProperty("os.name")) {
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

        });

        projectTree.setCellRenderer(new FileTreeCellRenderer());

        if (Context.getContext().getActiveProject() != null) {
            Context.getContext().getActiveProject().getExecModels().entrySet().forEach(verModel -> {
                JPanel newVerPanel = new JPanel();
                newVerPanel.setLayout(new BorderLayout());
                JXVersionTreeTable jxVersionTreeTable = new JXVersionTreeTable(verModel.getValue(), this);
                newVerPanel.add(jxVersionTreeTable);
                versionTabbedPane.addTab(verModel.getKey(), newVerPanel);
            });
        }

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
        versionTabbedPane = new JTabbedPane();
        testPane.setViewportView(versionTabbedPane);
        final JScrollPane scrollPane2 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        projectViewPane.add(scrollPane2, gbc);
        consoleTabbedPane = new JTabbedPane();
        consoleTabbedPane.setEnabled(true);
        consoleSplitPane.setRightComponent(consoleTabbedPane);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        consoleTabbedPane.addTab("系统控制台", panel1);
        consoleMsgArea = new JTextArea();
        consoleMsgArea.setEnabled(false);
        panel1.add(consoleMsgArea, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        consoleTabbedPane.addTab("系统控制台错误", panel2);
        consoleErrorArea = new JTextArea();
        consoleErrorArea.setEnabled(false);
        panel2.add(consoleErrorArea, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
