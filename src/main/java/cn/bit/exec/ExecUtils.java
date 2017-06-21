package cn.bit.exec;

import org.dom4j.Element;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

import javax.swing.event.TreeModelListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by zhehua on 11/04/2017.
 */
public class ExecUtils {
    public static final int ERROR_LINE = 2;
    public static final int WARNING_LINE = 1;
    public static final int NORMAL_LINE = 0;
    public static DefaultTreeTableModel loadTestExec(Element execElement) {
        DefaultTreeTableModel defaultTreeTableModel = new ExecTreeTableModel();

        ArrayList<String> colIds = new ArrayList<>();
        colIds.add("name");
        colIds.add("testCases");
        defaultTreeTableModel.setColumnIdentifiers(colIds);
        DefaultMutableTreeTableNode rootNode = new DefaultMutableTreeTableNode();
        defaultTreeTableModel.setRoot(rootNode);

        List<Element> testClassElements = execElement.elements();
        int classCount = 0;
        for (Element testClassElement : testClassElements) {
            ExecMutableTreeTableNode newClassNode =
                    new ExecMutableTreeTableNode(new TestEntity(testClassElement.attributeValue("name")));
            defaultTreeTableModel.insertNodeInto(newClassNode, rootNode, classCount);
            List<Element> testElements = testClassElement.elements();
            for(Element testElement : testElements) {
                buildTestExec(testElement, newClassNode);
            }
            classCount ++;
        }

        return defaultTreeTableModel;
    }

    public static void buildTestExec(Element testElement, ExecMutableTreeTableNode parentNode) {

        if(testElement.getName().equals("option")) {
            String isChecked = testElement.attributeValue("checked");
            parentNode.getTestEntity().testCases.add(testElement.attributeValue("name"));
            if(isChecked!= null && isChecked.equals("true")) {
                parentNode.getTestEntity().selectedCase = testElement.attributeValue("name");
            }
        } else if(testElement.getName().equals("testClass")) {
            ExecMutableTreeTableNode newClassNode =
                    new ExecMutableTreeTableNode(new TestEntity(testElement.attributeValue("name")));
            parentNode.insert(newClassNode, parentNode.getChildCount());
            List<Element> childElements = testElement.elements();
            for(Element childElement : childElements) {
                buildTestExec(childElement, newClassNode);
            }
        } else if(testElement.getName().equals("testEntry")) {
            List<String> stringOptionList = new ArrayList<>();
            ExecMutableTreeTableNode newTestNode = new ExecMutableTreeTableNode(new TestEntity(
                    stringOptionList,
                    testElement.attributeValue("name"),
                    testElement.attributeValue("arg"),
                    TestEntity.TestStatus.valueOf(testElement.attributeValue("status")),
                    null));
            parentNode.insert(newTestNode, parentNode.getChildCount());
            List<Element> childElements = testElement.elements();
            for(Element childElement : childElements) {
                buildTestExec(childElement, newTestNode);
            }
        }
    }

    public static int classifyInputStringFromMakefile(String line) {
        if (line.contains("Error")) {
            return 2;
        } else if (line.contains("Warning")) {
            return 1;
        } else {
            return 0;
        }
    }
}
