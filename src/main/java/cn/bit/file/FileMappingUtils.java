package cn.bit.file;

import cn.bit.Context;
import cn.bit.model.FileNodeEntity;
import org.apache.commons.io.IOUtils;
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

    public static void insertDefaultMapping(InputStream defaultXml, String newPrjXmlPath ,String newPrjPath) {
        try {
            Document defaultDocument = new SAXReader().read(defaultXml);
            try {
                Document iteDocument = new SAXReader().read(new FileInputStream(newPrjXmlPath));
                List<Element> defaultFileList = defaultDocument.getRootElement().elements("mappingEntry");
                Element iteMapping = iteDocument.getRootElement().element("userMapping");
                if(iteMapping == null) {
                    iteMapping = iteDocument.getRootElement().addElement("userMapping");
                }
                for (Element element:defaultFileList) {
                    Element newMappingEntry = iteMapping.addElement("mappingEntry");
                    newMappingEntry.addAttribute("abstractPath", element.attributeValue("abstractPath"));
                    newMappingEntry.addAttribute("absolutePath", newPrjPath + element.attributeValue("absolutePath"));
                }
                XMLWriter xmlWriter = null;
                try {
                    xmlWriter = new XMLWriter(new FileWriter(newPrjXmlPath));
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
            document = new SAXReader().read(new FileInputStream(XMLPath), "UTF8");

            Element userMap = document.getRootElement().element("userMapping");
            fileMapping.entrySet().stream().forEach((entry) -> {
                Element newMappingEntry = userMap.addElement("mappingEntry");
                newMappingEntry.addAttribute("abstractPath", entry.getKey());
                newMappingEntry.addAttribute("absolutePath", entry.getValue());
            });

            XMLWriter xmlWriter = new XMLWriter(new FileWriter(XMLPath));
            xmlWriter.write(document);
            xmlWriter.flush();
            xmlWriter.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public static void createNewProject(String configurePath, String newPrjName, String defaultXmlPath , String newPrjPath) {
        try {
            File newPrjXml = new File(newPrjPath + newPrjName + ".ite");
            newPrjXml.createNewFile();
            IOUtils.write("<projectModel>\n" + "</projectModel>", new FileOutputStream(newPrjXml.getPath()));
            Document configureDoc = new SAXReader().read(new FileInputStream(configurePath));
            Element prjConfigs = configureDoc.getRootElement();
            Element newPrj = prjConfigs.addElement("ActiveProject");
            newPrj.addAttribute("projectName", newPrjName);
            newPrj.addAttribute("projectFilePath", newPrjXml.getPath());

            XMLWriter xmlWriter = new XMLWriter(new FileWriter(configurePath));
            xmlWriter.write(configureDoc);
            xmlWriter.flush();

            insertDefaultMapping(new FileInputStream(defaultXmlPath), newPrjXml.getPath(), newPrjPath);

            Document document = new SAXReader().read(new FileInputStream(newPrjXml.getPath()));
            List<Element> newDirElementsList = document.getRootElement().element("userMapping").elements();

            for (Element newDirElement : newDirElementsList) {
                File newDir = new File(newDirElement.attributeValue("absolutePath"));
                newDir.mkdirs();
            }


        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean delDir(File dir) {
        if (!dir.isDirectory() || dir.list().length == 0) {
            return dir.delete();
        }
        return delDir(dir) && dir.delete();

    }

    // TODO test
    public static void removeMappingFromXml(String XmlPath, List<String> removedList, boolean isDeleteFile) {
        Document document = null;
        try {
            document = new SAXReader().read(new FileInputStream(XmlPath), "UTF8");
             List<Element> userMappings = document.getRootElement().element("userMapping").elements();

            Element userMappingElement = document.getRootElement().element("userMapping");

            removedList.stream().forEach(abstractPath -> {
                userMappings.stream()
                        .filter(element -> element.attributeValue("abstractPath").startsWith(abstractPath))
                        .forEach(element -> {
                            userMappingElement.remove(element);
                            if (isDeleteFile) {
                                File tmpFile = new File(element.attributeValue("absolutePath"));
                                delDir(tmpFile);
                            }
                        });
            });
            XMLWriter xmlWriter = new XMLWriter(new FileWriter(XmlPath));
            xmlWriter.write(document);
            xmlWriter.flush();
            xmlWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

    public static void closeProject(String prjToClose)
    {
        Document document = null;
        try {
            document = new SAXReader().read(new FileInputStream(Context.configureFilePath));
        List<Element> projectElementList = document.getRootElement().elements();
        for (Element projectElement: projectElementList) {
                if(projectElement.attributeValue("projectName").equals(prjToClose)) {
                    document.getRootElement().remove(projectElement);
                    Context.getOpenProjects().remove(prjToClose);
                    XMLWriter xmlWriter = null;
                    xmlWriter = new XMLWriter(new FileWriter(Context.configureFilePath));
                    xmlWriter.write(document);
                    xmlWriter.flush();
                    xmlWriter.close();
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }
}
