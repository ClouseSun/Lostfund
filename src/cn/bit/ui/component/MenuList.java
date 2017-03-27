package cn.bit.ui.component;

/**
 * Created by KlousesSun on 2017/3/27.
 */
public class MenuList {
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
