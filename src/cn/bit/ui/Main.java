package cn.bit.ui;

import cn.bit.parser.MenuBarGson;
import cn.bit.ui.builder.MenuBarBuilder;
import cn.bit.ui.component.MenuBar;

import javax.swing.*;

/**
 * Created by zhehua on 19/03/2017.
 */
public class Main {
    private JPanel panel1;

    public static void main(String[] args) {
            MenuBarGson menuBarGson = new MenuBarGson("/json/menubar_hierachy");
            MenuBar menuBar = menuBarGson.parse();
            MenuBarBuilder menuBarBuilder = new MenuBarBuilder(menuBar);

            JFrame f = new JFrame();
            f.setBounds(300, 100, 400, 300);
            f.setSize(800,600);
            menuBarBuilder.build(f);
            f.setVisible(true);
        }
}
