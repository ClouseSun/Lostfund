package cn.bit.file;

import cn.bit.model.FileNodeEntity;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KlousesSun on 2017/3/23.
 */
public class FileMappingUtils {

    public static  Map<String, String> build(InputStream xmlStream) {
        Map<String, String> fileMap = new LinkedHashMap<>();

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
        return new LinkedHashMap<>();
    }

    public static void insertDefaultMapping(InputStream defaultXml, String itePath) {
        try {
            Document defaultDocument = new SAXReader().read(defaultXml);
            try {
                Document iteDocument = new SAXReader().read(new FileInputStream(itePath));
                List<Element> defaultFileList = defaultDocument.getRootElement().elements("mappingEntry");
                Element iteMapping = iteDocument.getRootElement().element("userMapping");
                for (Element element:defaultFileList) {
                    Element newMappingEntry = iteMapping.addElement("mappingEntry");
                    newMappingEntry.addAttribute("abstractPath", element.attributeValue("abstractPath"));
                    newMappingEntry.addAttribute("absolutePath", element.attributeValue("absolutePath"));
                }
                XMLWriter xmlWriter = null;
                try {
                    xmlWriter = new XMLWriter(new FileWriter(itePath));
                    xmlWriter.write(iteDocument);
                    xmlWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public static void insertNewMapping(
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

    }

    public static String path2String(TreeNode[] pathNodes) {
        StringBuilder path = new StringBuilder();
        for(int i = 0; i < pathNodes.length; i++) {
            String dir = ((FileNodeEntity) ((DefaultMutableTreeNode) pathNodes[i]).getUserObject()).getAbstractName();
            if(dir != null) {
                path.append(dir + "/");
            }
        }
        return path.toString();
    }
}
