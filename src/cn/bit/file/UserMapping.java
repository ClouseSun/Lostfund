package cn.bit.file;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by KlousesSun on 2017/3/23.
 */
public class UserMapping extends HashMap<String, String>{

    String XMLPath;

    public UserMapping() {
        super();
    }

    public void build(String XMLPath) {
        this.XMLPath = XMLPath;
        try {
            Document document = null;
            try {
                document = new SAXReader().read(new FileInputStream(XMLPath), "UTF8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            List<Element> elementList = document.getRootElement().element("userMapping").elements("mappingEntry");
            for (Element element:elementList) {
                this.put(element.attributeValue("abstractPath"), element.attributeValue("absolutePath"));
            }
            elementList = document.getRootElement().element("defaultMapping").elements("mappingEntry");
            for (Element element:elementList) {
                this.put(element.attributeValue("abstractPath"), element.attributeValue("absolutePath"));
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void insertNewMapping(String abstractFilePath, String realFilePath) {
        this.put(abstractFilePath, realFilePath);

        try {
            Document document = null;
            try {
                document = new SAXReader().read(new FileInputStream(XMLPath), "UTF8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Element userMap = document.getRootElement().element("userMapping");
            Element newMappingEntry = userMap.addElement("mappingEntry");
            newMappingEntry.addAttribute("abstractPath", abstractFilePath);
            newMappingEntry.addAttribute("absolutePath", realFilePath);
            try {
                XMLWriter xmlWriter = new XMLWriter(new FileWriter(XMLPath));
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
