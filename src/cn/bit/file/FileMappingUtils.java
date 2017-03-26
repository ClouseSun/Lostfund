package cn.bit.file;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KlousesSun on 2017/3/23.
 */
public class FileMappingUtils {

    public static  Map<String, String> build(InputStream xmlStream) {
        Map<String, String> fileMap = new HashMap<>();

        try {
            Document document = new SAXReader().read(xmlStream);
            List<Element> elementList = document.getRootElement().element("userMapping").elements("mappingEntry");
            for (Element element:elementList) {
                fileMap.put(element.attributeValue("abstractPath"), element.attributeValue("absolutePath"));
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return fileMap;
    }

    public static Map<String, String> build(String xmlFilePath) {
        try {
            return build(new FileInputStream(xmlFilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    public static void insertNewMapping(Map<String, String> fileMap,
                                 String XMLPath,
                                 String abstractFilePath,
                                 String realFilePath) {
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

        fileMap.put(abstractFilePath, realFilePath);
    }
}
