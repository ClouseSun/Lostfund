package cn.bit.ui;

import cn.bit.file.AbstractFileTree;
import cn.bit.file.FileMappingUtils;
import cn.bit.ui.component.JsonMenuBar;
import cn.bit.ui.component.JsonTreePopMenu;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.io.IOException;
import java.util.Map;

/**
 * Created by zhehua on 19/03/2017.
 */
public class Main {
    private JPanel mainPanel;
    private JScrollPane PrjPane;
    private JPanel panel1;

    private JTree tree1;

    public static void main(String[] args) {
        Main mainObj = new Main();
        JMenuBar menuBar = null;

        JFrame frame = new JFrame("Main");
        frame.setContentPane(mainObj.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        // build menubar from resource
        try {
            String jsonMenuBarString = IOUtils.toString(mainObj.getClass().getResourceAsStream("/json/menubar_hierachy"), "UTF8");
            menuBar = new JsonMenuBar(jsonMenuBarString);
            frame.setJMenuBar(menuBar);
        } catch(IOException e) {
            e.printStackTrace();
        }
        AbstractFileTree abstractFileTree = new AbstractFileTree();
        Map<String, String> abstractFileMap = FileMappingUtils.build("res/raw/test_project");
        abstractFileTree.addAll(abstractFileMap);

        mainObj.tree1.setModel(abstractFileTree.model());

        try {
            String jsonPopMenuString = IOUtils.toString(mainObj.getClass().getResourceAsStream("/json/popmenu_hierachy"),"UTF8");
            new JsonTreePopMenu(jsonPopMenuString, mainObj.tree1);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            FileMappingUtils.insertDefaultMapping(abstractFileMap, new FileInputStream("res/raw/default_file_map"), "res/raw/test_project");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }


        frame.setVisible(true);
    }


}
