package cn.bit.ui;

import cn.bit.ui.component.JsonMenuBar;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by zhehua on 19/03/2017.
 */
public class Main {
    private JPanel mainPanel;
    private JTree tree1;
    private JTree tree2;
    private JTree tree3;


    public static void main(String[] args) {
        Main mainObj = new Main();
        JMenuBar menuBar = null;

        JFrame frame = new JFrame("Main");
        frame.setContentPane(mainObj.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        // build menubar from resource
        try {
            String jsonString = IOUtils.toString(mainObj.getClass().getResourceAsStream("/json/menubar_hierachy"), "UTF8");
            menuBar = new JsonMenuBar(jsonString);
            frame.setJMenuBar(menuBar);
        } catch(IOException e) {
            e.printStackTrace();
        }

        frame.setVisible(true);
    }
}
