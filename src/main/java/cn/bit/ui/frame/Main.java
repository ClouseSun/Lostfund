package cn.bit.ui.frame;

import cn.bit.Context;
import cn.bit.ui.component.JsonMenuBar;
import cn.bit.ui.component.JsonTreePopMenu;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
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
    private JScrollPane PrjPane;
    private JPanel panel1;

    private JTree tree1;
    private JSplitPane consoleSplitPane;
    private JSplitPane editorSplitPane;

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

        DefaultTreeModel iteTreeModel = new DefaultTreeModel(new DefaultMutableTreeNode(), true);

        Context.init(Context.configureFilePath);

        Context.getContext().getOpenProjects().entrySet().stream().forEach(stringIteProjectEntry -> {
            iteTreeModel.insertNodeInto(stringIteProjectEntry.getValue().getProjectTree().getProjectTreeRoot()
                    , ((DefaultMutableTreeNode) iteTreeModel.getRoot())
                    , ((DefaultMutableTreeNode) iteTreeModel.getRoot()).getChildCount());
        });

        try {
            String jsonMenuBarString = IOUtils.toString(new FileInputStream(Context.getJsonMenuBarPath()));
            menuBar = new JsonMenuBar(jsonMenuBarString, tree1);
            mainFrame.setJMenuBar(menuBar);
        } catch (IOException e) {
            e.printStackTrace();
        }

        tree1.setModel(iteTreeModel);
        tree1.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                TreePath treePath = tree1.getPathForLocation(e.getX(), e.getY());
                if (treePath == null) {
                    return;
                }
                tree1.setSelectionPath(treePath);


                if (e.getButton() == MouseEvent.BUTTON3) {
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
                    new JsonTreePopMenu(tree1).show(tree1, e.getX(), e.getY());
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
        consoleSplitPane.setDividerLocation(0);
        consoleSplitPane.setDividerSize(3);
        consoleSplitPane.setDoubleBuffered(false);
        consoleSplitPane.setInheritsPopupMenu(true);
        consoleSplitPane.setOneTouchExpandable(true);
        consoleSplitPane.setOrientation(0);
        consoleSplitPane.setResizeWeight(0.0);
        mainPanel.add(consoleSplitPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        editorSplitPane = new JSplitPane();
        editorSplitPane.setContinuousLayout(true);
        editorSplitPane.setDividerLocation(0);
        editorSplitPane.setDividerSize(3);
        editorSplitPane.setEnabled(true);
        consoleSplitPane.setLeftComponent(editorSplitPane);
        panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        editorSplitPane.setLeftComponent(panel1);
        PrjPane = new JScrollPane();
        PrjPane.setPreferredSize(new Dimension(-1, -1));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(PrjPane, gbc);
        tree1 = new JTree();
        tree1.setRootVisible(false);
        tree1.setShowsRootHandles(true);
        tree1.putClientProperty("JTree.lineStyle", "");
        PrjPane.setViewportView(tree1);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(scrollPane1, gbc);
        final JScrollPane scrollPane2 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(scrollPane2, gbc);
        final JScrollPane scrollPane3 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(scrollPane3, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
