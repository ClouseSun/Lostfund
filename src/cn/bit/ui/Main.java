package cn.bit.ui;

import cn.bit.Context;
import cn.bit.file.AbstractFileTree;
import cn.bit.file.FileMappingUtils;
import cn.bit.model.IteProject;
import cn.bit.ui.component.JsonMenuBar;
import cn.bit.ui.component.JsonTreePopMenu;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

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
        setSize(1440, 900);

        JMenuBar menuBar = null;

        // loadAbs2RealMap menubar from resource
        try {
            String jsonMenuBarString = IOUtils.toString(getClass().getResourceAsStream("/json/menubar_hierachy"), "UTF8");
            menuBar = new JsonMenuBar(jsonMenuBarString);
            setJMenuBar(menuBar);
        } catch(IOException e) {
            e.printStackTrace();
        }

        DefaultTreeModel iteTreeModel = new DefaultTreeModel(new DefaultMutableTreeNode());

        Context.init(configureFilePath);

        Context.getContext().getOpenProjects().entrySet().stream().forEach(stringIteProjectEntry -> {
            iteTreeModel.insertNodeInto(stringIteProjectEntry.getValue().getProjectTree().getProjectTreeRoot()
            , ((DefaultMutableTreeNode) iteTreeModel.getRoot())
                    , ((DefaultMutableTreeNode) iteTreeModel.getRoot()).getChildCount());
        });

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
