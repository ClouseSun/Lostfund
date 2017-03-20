package cn.bit.ui.builder;

import cn.bit.ui.component.MenuBar;
import cn.bit.ui.component.MenuList;

import javax.swing.*;

/**
 * Created by KlousesSun on 2017/3/20.
 */
public class MenuBarBuilder {
    MenuBar menuBar;

    public MenuBarBuilder(MenuBar menuBar) {
        this.menuBar = menuBar;
    }

    public void build(JFrame jFrame) {
        JMenuBar jMenuBar = new JMenuBar();
        for (MenuList ml:menuBar.menuList) {
            JMenu jMenu = new JMenu(ml.title);
            jMenuBar.add(jMenu);
            for(MenuList mll:ml.menuList) {
                dfs(jMenu, mll);
            }
        }
        jFrame.setJMenuBar(jMenuBar);
    }

    public void dfs(JMenu father, MenuList menuList) {
        if(menuList.menuList == null) {
            JMenuItem jMenuItem = new JMenuItem(menuList.title);
            father.add(jMenuItem);
        }
        else {
            JMenu jMenu = new JMenu(menuList.title);
            for (MenuList ml:menuList.menuList) {
                dfs(jMenu, ml);
            }
            father.add(jMenu);
        }
    }
}
