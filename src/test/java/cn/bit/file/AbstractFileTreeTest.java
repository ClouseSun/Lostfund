//package cn.bit.file;
//
//import cn.bit.model.FileNodeEntity;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import javax.swing.tree.DefaultMutableTreeNode;
//import java.util.Enumeration;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.junit.Assert.*;
//
///**
// * Created by zhehua on 23/03/2017.
// */
//public class AbstractFileTreeTest {
//    Map<String, String> fileMap;
//    AbstractFileTree fileTree;
//    @Before
//    public void setUp() throws Exception {
//        fileMap = new HashMap<>();
//        fileTree = new AbstractFileTree();
//    }
//
//    @After
//    public void tearDown() throws Exception {
//
//    }
//
//
//    @Test
//    public void buildTest1() throws Exception {
//        fileMap.put("b/f", "patha");
//        fileMap.put("b/c", "pathb");
//        fileMap.put("b/d", "pathc");
//        fileMap.put("e/a", "pathd");
//        fileMap.put("e/k/", "dir");
//        fileMap.put("e/k//m", "pathm");
//
//        fileTree.addAll(fileMap);
//
//        FileTreeModel model = fileTree.model();
//        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();
//        assertTrue(rootNode != null);
//        assertTrue(((FileNodeEntity)rootNode.getUserObject()).getNodeType() == FileNodeEntity.NODE_TYPE_ROOT);
//
//        assertEquals(2, model.getChildCount(rootNode));
//        Enumeration rootChildren = rootNode.children();
//        while(rootChildren.hasMoreElements()) {
//            DefaultMutableTreeNode node = (DefaultMutableTreeNode) rootChildren.nextElement();
//            assertTrue(node.getChildCount() == 3 || node.getChildCount() == 1);
//        }
//
//    }
//
//    @Test
//    public void buildTest2() throws Exception {
//        fileMap.put("b/a", "patha");
//        fileMap.put("c/e", "pathb");
//        fileMap.put("d/f", "pathc");
//
//        fileTree.addAll(fileMap);
//
//        FileTreeModel model = fileTree.model();
//        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();
//        assertTrue(rootNode != null);
//        assertTrue(((FileNodeEntity)rootNode.getUserObject()).getNodeType() == FileNodeEntity.NODE_TYPE_ROOT);
//
//        assertEquals(3, model.getChildCount(rootNode));
//        Enumeration rootChildren = rootNode.children();
//        while(rootChildren.hasMoreElements()) {
//            DefaultMutableTreeNode node = (DefaultMutableTreeNode) rootChildren.nextElement();
//            assertEquals(1, node.getChildCount());
//        }
//
//    }
//}