package cn.bit.ui.frame;

import cn.bit.Context;
import cn.bit.model.FileNodeEntity;
import cn.bit.ui.component.JsonMenuBar;
import cn.bit.ui.component.JsonTreePopMenu;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.apache.commons.io.IOUtils;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

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
                                            + selectedNode.getRealPath().substring(0, selectedNode.getRealPath().lastIndexOf('/')));
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


//        jxTreeTable = new JXTreeTable(Context.getContext().getExecModel());
//        Version1.add(jxTreeTable);

        //jxTreeTable.setDefaultEditor();
        mainFrame.setVisible(true);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                consoleSplitPane.setDividerLocation(0.5);
                editorSplitPane.setDividerLocation(0.5);
            }
        });
    }

}
