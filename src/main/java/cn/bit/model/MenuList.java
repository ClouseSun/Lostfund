package cn.bit.model;

/**
 * Created by KlousesSun on 2017/3/27.
 */
public class MenuList {
    private String title;
    private String name;
    private boolean isEnable;
    private MenuList menuList[];

    public MenuList(String title, String name, boolean isEnable, MenuList[] menuList) {
        this.title = title;
        this.name = name;
        this.isEnable = isEnable;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }
}
