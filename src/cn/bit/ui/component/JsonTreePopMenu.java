package cn.bit.ui.component;

import cn.bit.file.AbstractFileTree;
import com.google.gson.Gson;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by KlousesSun on 2017/3/27.
 */
public class JsonTreePopMenu extends JPopupMenu implements MouseListener {

    private JTree jTree;
    private MenuList menuList;

    public JsonTreePopMenu(MenuList menuList) {
        this.menuList = menuList;
        for (MenuList ml:menuList.getMenuList()) {
            if(ml.getMenuList() == null) {
                JMenuItem jMenuItem = new JMenuItem(ml.getTitle());
                add(jMenuItem);
            }
            else {
                JMenu jMenu = new JMenu(ml.getTitle());
                add(jMenu);
                if (ml.getMenuList() != null) {
                    for (MenuList mll : ml.getMenuList()) {
                        buildMenuByList(jMenu, mll);
                    }
                }
            }
        }
    }

    public JsonTreePopMenu(String jsonString, JTree jTree) {
        this(new Gson().fromJson(jsonString, MenuList.class));
        this.jTree = jTree;
        jTree.addMouseListener(this);
    }


    public void buildMenuByList(JMenu root, MenuList menuList) {
        if(menuList.getMenuList() == null) {
            JMenuItem jMenuItem = new JMenuItem(menuList.getTitle());
            root.add(jMenuItem);
        }
        else {
            JMenu jMenu = new JMenu(menuList.getTitle());
            for (MenuList ml:menuList.getMenuList()) {
                buildMenuByList(jMenu, ml);
            }
            root.add(jMenu);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        TreePath treePath = jTree.getPathForLocation(e.getX(), e.getY());
        if(treePath == null) {
            return;
        }
        jTree.setSelectionPath(treePath);

        if(e.getButton() == 3) {
            show(jTree, e.getX(), e.getY());
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
}
