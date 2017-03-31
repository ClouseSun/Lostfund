package cn.bit.ui.component;

import com.google.gson.Gson;

import javax.swing.*;

/**
 * Created by KlousesSun on 2017/3/19.
 */
public class JsonMenuBar extends JMenuBar {
    private MenuList menuList;

    public JsonMenuBar(MenuList menuList) {
        this.menuList = menuList;

        for (MenuList ml:menuList.getMenuList()) {
            if(ml.getMenuList() == null) {
                JMenuItem jMenuItem = new JMenuItem(menuList.getTitle());
                jMenuItem.setEnabled(menuList.isEnable());
                add(jMenuItem);
            }
            else {
                JMenu jMenu = new JMenu(ml.getTitle());
                jMenu.setEnabled(ml.isEnable());
                add(jMenu);
                if (ml.getMenuList() != null) {
                    for (MenuList mll : ml.getMenuList()) {
                        buildMenuByList(jMenu, mll);
                    }
                }
            }
        }
    }

    public JsonMenuBar(String jsonString) {
        this(new Gson().fromJson(jsonString, MenuList.class));
    }

    protected void buildMenuByList(JMenu root, MenuList menuList) {
        if(menuList.getMenuList() == null) {
            JMenuItem jMenuItem = new JMenuItem(menuList.getTitle());
            jMenuItem.setEnabled(menuList.isEnable());
            root.add(jMenuItem);
        }
        else {
            JMenu jMenu = new JMenu(menuList.getTitle());
            jMenu.setEnabled(menuList.isEnable());
            for (MenuList ml:menuList.getMenuList()) {
                buildMenuByList(jMenu, ml);
            }
            root.add(jMenu);
        }
    }



}
