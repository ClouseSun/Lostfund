package cn.bit;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by KlousesSun on 2017/3/22.
 */
public class TestProject {
    public static void main (String[] args){
        TestProject testProject = new TestProject();

        testProject.insertUserMapXML("123", "456");

//        try {
//            Document document = new SAXReader().read(testProject.getClass().getResourceAsStream("/raw/test_project"));
//            HashMap<String, String> pathMap = new HashMap<String, String>();
//            List<Element> elementList = document.getRootElement().element("userMapping").elements("mappingEntry");
//            for (Element element:elementList) {
//                pathMap.put(element.attributeValue("abstractPath"), element.attributeValue("absolutePath"));
//            }
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
    }

    public void insertUserMapXML(String abstractPath, String absolutePath) {
        TestProject testProject = new TestProject();

        try {
            Document document = new SAXReader().read(testProject.getClass().getResourceAsStream("/raw/test_project"));
            Element userMap = document.getRootElement().element("userMapping");
            Element newMappingEntry = userMap.addElement("mappingEntry");
            newMappingEntry.addAttribute("abstractPath", abstractPath);
            newMappingEntry.addAttribute("absolutePath", absolutePath);

            try {
                XMLWriter xmlWriter = new XMLWriter(new FileWriter("/Users/KlousesSun/IdeaProjects/ITE315/XMLtest"));
                xmlWriter.write(document);
                xmlWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
