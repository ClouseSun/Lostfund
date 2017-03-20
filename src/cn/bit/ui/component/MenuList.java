package cn.bit.ui.component;

/**
 * Created by KlousesSun on 2017/3/19.
 */
public class MenuList {
    public String title;
    public MenuList menuList[];

    public MenuList(String title, MenuList menuList[]) {
        this.title = title;
        this.menuList = menuList;
    }
}
