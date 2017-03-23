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

        for (MenuList ml:menuList.menuList) {
            JMenu jMenu = new JMenu(ml.title);
            add(jMenu);
            for(MenuList mll:ml.menuList) {
                buildMenuByList(jMenu, mll);
            }
        }
    }

    public JsonMenuBar(String jsonString) {
        this(new Gson().fromJson(jsonString, MenuList.class));
    }

    protected void buildMenuByList(JMenu root, MenuList menuList) {
        if(menuList.menuList == null) {
            JMenuItem jMenuItem = new JMenuItem(menuList.title);
            root.add(jMenuItem);
        }
        else {
            JMenu jMenu = new JMenu(menuList.title);
            for (MenuList ml:menuList.menuList) {
                buildMenuByList(jMenu, ml);
            }
            root.add(jMenu);
        }
    }


    public static class MenuList {
        private String title;
        private MenuList menuList[];

        public MenuList(String title, MenuList menuList[]) {
            this.title = title;
            this.menuList = menuList;
        }

        public MenuList() {
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public MenuList[] getMenuList() {
            return menuList;
        }

        public void setMenuList(MenuList[] menuList) {
            this.menuList = menuList;
        }
    }



}
