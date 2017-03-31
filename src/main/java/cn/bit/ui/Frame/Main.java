package cn.bit.ui.Frame;

import cn.bit.Context;
import cn.bit.ui.component.JsonMenuBar;
import cn.bit.ui.component.JsonTreePopMenu;
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
public class Main extends JFrame{
    public static String configureFilePath = "res/raw/configure";

    private JPanel mainPanel;
    private JScrollPane PrjPane;
    private JPanel panel1;

    private JTree tree1;

    public static void main(String[] args) {
        Main mainObj = new Main();
        mainObj.setVisible(true);
    }


    public Main() {
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        setSize((int)screenSize.getWidth(), (int)screenSize.getWidth());

        JMenuBar menuBar = null;

        // loadAbs2RealMap menubar from resource

        DefaultTreeModel iteTreeModel = new DefaultTreeModel(new DefaultMutableTreeNode());

        Context.init(configureFilePath);

        Context.getContext().getOpenProjects().entrySet().stream().forEach(stringIteProjectEntry -> {
            iteTreeModel.insertNodeInto(stringIteProjectEntry.getValue().getProjectTree().getProjectTreeRoot()
            , ((DefaultMutableTreeNode) iteTreeModel.getRoot())
                    , ((DefaultMutableTreeNode) iteTreeModel.getRoot()).getChildCount());
        });

        try {
            String jsonMenuBarString = IOUtils.toString(new FileInputStream(Context.getJsonMenuBarPath()));
            menuBar = new JsonMenuBar(jsonMenuBarString);
            setJMenuBar(menuBar);
        } catch(IOException e) {
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
    }
}
