package cn.bit.ui;

import cn.bit.parser.MenuBarGson;
import cn.bit.ui.builder.MenuBarBuilder;
import cn.bit.ui.component.MenuBar;

import javax.swing.*;

/**
 * Created by zhehua on 19/03/2017.
 */
public class Main {
    private JPanel mainPanel;


    public static void main(String[] args) {
        MenuBarGson menuBarGson = new MenuBarGson("/json/menubar_hierachy");
        MenuBar menuBar = menuBarGson.parse();
        MenuBarBuilder menuBarBuilder = new MenuBarBuilder(menuBar);

        JFrame frame = new JFrame("Main");
        frame.setContentPane(new Main().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        menuBarBuilder.build(frame);
        frame.setVisible(true);
    }
}
