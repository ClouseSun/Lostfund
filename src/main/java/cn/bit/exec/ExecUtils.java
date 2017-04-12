package cn.bit.exec;

import org.dom4j.Element;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by zhehua on 11/04/2017.
 */
public class ExecUtils {
    public static DefaultTreeTableModel loadTestExec(Element execElement) {
        DefaultTreeTableModel defaultTreeTableModel = new DefaultTreeTableModel();
        ArrayList<String> colIds = new ArrayList<>();
        colIds.add("name");
        colIds.add("options");
        defaultTreeTableModel.setColumnIdentifiers(colIds);
        DefaultMutableTreeTableNode rootNode = new DefaultMutableTreeTableNode();
        defaultTreeTableModel.setRoot(rootNode);

        List<Element> testClassElements = execElement.elements();
        List<Element> testEntryElements;
        int classCount = 0;
        for (Element testClassElement : testClassElements) {
            ExecMutableTreeTableNode newClassNode =
                    new ExecMutableTreeTableNode(new TestEntity(testClassElement.attributeValue("name")));
            defaultTreeTableModel.insertNodeInto(newClassNode, rootNode, classCount);
            testEntryElements = testClassElement.elements();
            int testCount = 0;
            for (Element testEntryElement : testEntryElements) {
                List<Element> optionElements = testEntryElement.elements();
                List<String> stringOptionList = new ArrayList<>();
                int optionCount = 0;
                for (Element optionElement : optionElements) {
                    String isChecked = optionElement.attributeValue("checked");
                    stringOptionList.add(optionElement.attributeValue("name"));
                    if (isChecked == null || !isChecked.equals("true")) {
                        optionCount++;
                    }
                }
                ExecMutableTreeTableNode newTestNode = new ExecMutableTreeTableNode(new TestEntity(
                        stringOptionList,
                        testEntryElement.attributeValue("name"),
                        testEntryElement.attributeValue("arg"),
                        TestEntity.TestStatus.valueOf(testEntryElement.attributeValue("status"))
                ));
                defaultTreeTableModel.insertNodeInto(newTestNode, newClassNode, testCount);
                testCount ++;
            }

            classCount ++;
        }



        return defaultTreeTableModel;
    }

}
