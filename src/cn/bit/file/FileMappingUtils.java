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

    public static Map<String, String> loadFileMapping(InputStream xmlStream, boolean isAbs2Real) {
        Map<String, String> fileMap = new LinkedHashMap<>();

        try {
            Document document = new SAXReader().read(xmlStream);
            List<Element> elementList = document.getRootElement().element("userMapping").elements("mappingEntry");
            for (Element element:elementList) {
                if (isAbs2Real) {
                    fileMap.put(element.attributeValue("abstractPath"), element.attributeValue("absolutePath"));
                } else {
                    fileMap.put(element.attributeValue("absolutePath"), element.attributeValue("abstractPath"));
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return fileMap;
    }

    public static Map<String, String> loadFileMapping(String xmlFilePath, boolean isAbs2Real) {
        try {
            return loadFileMapping(new FileInputStream(xmlFilePath), true);
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

    public static void insertNewMapping(String XMLPath, Map<String, String> fileMapping) {
        try {
            Document document = null;
            try {
                document = new SAXReader().read(new FileInputStream(XMLPath), "UTF8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Element userMap = document.getRootElement().element("userMapping");
            fileMapping.entrySet().stream().forEach((entry) -> {
                Element newMappingEntry = userMap.addElement("mappingEntry");
                newMappingEntry.addAttribute("abstractPath", entry.getKey());
                newMappingEntry.addAttribute("absolutePath", entry.getValue());
            });

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

    public static void removeMappingFromXml(String XmlPath, Map<Integer, String> removeMap) {
        try {
            Document document = null;
            try {
                document = new SAXReader().read(new FileInputStream(XmlPath), "UTF8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            List<Element> userMappings = document.getRootElement().element("userMapping").elements();

            Document finalDocument = document;

            removeMap.entrySet().stream().forEach((integerStringEntry -> {
                if(integerStringEntry.getKey() == FileNodeEntity.NODE_TYPE_FILE) {
                    userMappings.stream()
                            .filter(element -> removeMap.containsValue(element.attributeValue("abstractPath")))
                            .forEach(element -> finalDocument.getRootElement().element("userMapping").remove(element));
                }
                else if(integerStringEntry.getKey() == FileNodeEntity.NODE_TYPE_DIR) {
                }
            }
            ));

            try {
                XMLWriter xmlWriter = new XMLWriter(new FileWriter(XmlPath));
                xmlWriter.write(document);
                xmlWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public static String path2String(TreeNode[] pathNodes, int nodeType) {
        StringBuilder path = new StringBuilder();
        for(int i = 0; i < pathNodes.length; i++) {
            String dir = ((FileNodeEntity) ((DefaultMutableTreeNode) pathNodes[i]).getUserObject()).getAbstractName();
            if (dir != null) {
                path.append(dir);
                if (i != pathNodes.length - 1) {
                    path.append('/');
                }
            }
        }
        if(nodeType == FileNodeEntity.NODE_TYPE_DIR)
            path.append("/");

        return path.toString();
    }
}
