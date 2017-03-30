//package cn.bit.file;
//
//import cn.bit.model.FileNodeEntity;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import javax.swing.tree.DefaultMutableTreeNode;
//import java.util.Enumeration;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
///**
// * Created by KlousesSun on 2017/3/23.
// */
//public class XML2AbsFileTreeTest {
//    String XMLPath1 = "/Users/KlousesSun/Desktop/XMLtest1";
//    String XMLPath2 = "/Users/KlousesSun/Desktop/XMLtest2";
//    AbstractFileTree fileTree;
//    @Before
//    public void setUp() throws Exception {
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
//
//        fileTree.addAll(FileMappingUtils.loadFileMapping(XMLPath1, true));
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
//        fileTree.addAll(FileMappingUtils.loadFileMapping(XMLPath2, true));
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
