package cn.bit;

import cn.bit.parser.MenuBarGson;
import cn.bit.ui.builder.MenuBarBuilder;
import cn.bit.ui.component.*;
import cn.bit.ui.component.MenuBar;
import com.google.gson.Gson;

import javax.swing.*;

/**
 * Created by KlousesSun on 2017/3/20.
 */
public class Main {

    public static void main(String[] args) {
        MenuBarGson menuBarGson = new MenuBarGson("/json/menubar_hierachy");
        MenuBar menuBar = menuBarGson.parse();
        MenuBarBuilder menuBarBuilder = new MenuBarBuilder(menuBar);

        JFrame f = new JFrame();
        f.setBounds(300, 100, 400, 300);
        f.setSize(800,600);
        f.setVisible(true);
        menuBarBuilder.build(f);
    }
}
