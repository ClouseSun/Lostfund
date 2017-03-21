package cn.bit.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhehua on 21/03/2017.
 */
public class Tree<T> {
    TreeNode<T> root;

    public static class TreeNode<T> {
        TreeNode parent;
        T data;
        List<TreeNode> childList = new LinkedList<>();

        public boolean isLeaf() {
            return childList.isEmpty();
        }

        public int getChildCount() {
            return childList.size();
        }

        public int getIndexOfChild(TreeNode child) {
            return childList.indexOf(child);
        }
    }
}
