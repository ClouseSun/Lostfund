package cn.bit.file;

import cn.bit.Context;
import cn.bit.model.FileNodeEntity;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.io.*;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KlousesSun on 2017/3/23.
 */
public class FileMappingUtils {

    /**
     * Get the abstract and absolute path string from the given XML file InputStream and put them into a map.
     * @param isAbs2Real true for abstract-absolute order of the key-value pair in map, false otherwise.
     * @return the file map.
     */
    public static Map<String, String> loadFileMapping(Element userMappingElement, boolean isAbs2Real) {
        Map<String, String> fileMap = new LinkedHashMap<>();

        List<Element> elementList = userMappingElement.elements("mappingEntry");
        for (Element element:elementList) {
            if (isAbs2Real) {
                fileMap.put(element.attributeValue("abstractPath"), element.attributeValue("absolutePath"));
            } else {
                fileMap.put(element.attributeValue("absolutePath"), element.attributeValue("abstractPath"));
            }
        }

        return fileMap;
    }

    /**
     * Called when creating new project to insert file mappings of default hierarchy into the XML file of the
     * new project.
     * @param defaultXml the InputStream of default hierarchy file.
     * @param rootElement the root element of the new project XML.
     * @param newPrjPath string of the new project path.
     */
    public static void insertDefaultMapping(InputStream defaultXml, Element rootElement, String newPrjPath) {
        try {
                Document defaultDocument = new SAXReader().read(defaultXml);
                List<Element> defaultFileList = defaultDocument.
                                                getRootElement().
                                                element("defaultMapping").
                                                elements("mappingEntry");
                Element userMapping = rootElement.element("userMapping");
                for (Element element : defaultFileList) {
                    Element newMappingEntry = userMapping.addElement("mappingEntry");
                    newMappingEntry.addAttribute("abstractPath", element.attributeValue("abstractPath"));
                    newMappingEntry.addAttribute("absolutePath",
                            newPrjPath + element.attributeValue("absolutePath"));
                }
                Element defaultExecRoot = defaultDocument.getRootElement().element("defaultExecStatus");
                Element cloneExecRoot = defaultExecRoot.createCopy();
                cloneExecRoot.setQName(new QName("execStatus"));
                rootElement.add(cloneExecRoot);

            } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Insert user file mappings into the given XML file.
     * @param fileMapping key-value pair representing abstract/absolute path.
     * */
    public static void insertNewMapping(String XMLPath, Map<String, String> fileMapping) {
        try {
            Document document = new SAXReader().read(new FileInputStream(XMLPath), "UTF8");

            Element userMap = document.getRootElement().element("userMapping");
            fileMapping.entrySet().stream().forEach((entry) -> {
                Element newMappingEntry = userMap.addElement("mappingEntry");
                newMappingEntry.addAttribute("abstractPath", entry.getKey());
                newMappingEntry.addAttribute("absolutePath", entry.getValue());
            });

            XMLWriter xmlWriter = new XMLWriter(new FileWriter(XMLPath), OutputFormat.createPrettyPrint());
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

    /**
     * Insert one user file mapping into the given XML file.
     * */
    public static void insertNewMapping(String XMLPath, String abstractPath, String absolutePath) {
        try {
            Document document = new SAXReader().read(new FileInputStream(XMLPath), "UTF8");
            Element userMap = document.getRootElement().element("userMapping");
            Element newMappingEntry = userMap.addElement("mappingEntry");
            newMappingEntry.addAttribute("abstractPath", abstractPath);
            newMappingEntry.addAttribute("absolutePath", absolutePath);

            XMLWriter xmlWriter = new XMLWriter(new FileWriter(XMLPath), OutputFormat.createPrettyPrint());
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

    /**
     * Called when creating a new project, create a new XML file in extension of ".ite" and insert default mappings
     * into it.
     * Register the name and path of the XML file of new project in the configure file.
     * Build dirs in real path of the new project.
     * @param newPrjPath the full path of the new project ends with '/'.
     * */
    public static void createNewProject(String newPrjName, String defaultXmlPath, String newPrjPath) {
        try {
            File newPrjXml = new File(newPrjPath + newPrjName + ".ite");
            newPrjXml.createNewFile();

            XMLWriter newXmlWriter = new XMLWriter(new FileWriter(newPrjXml.getPath()),
                    OutputFormat.createPrettyPrint());
            Document newXmlDoc = DocumentHelper.createDocument();
            Element root = DocumentHelper.createElement("projectModel");
            root.addAttribute("projectName", newPrjName);
            newXmlDoc.setRootElement(root);
            root.addElement("userMapping");

            Document configureDoc = new SAXReader().read(new FileInputStream(Context.configureFilePath));
            Element prjConfigs = configureDoc.getRootElement();
            Element newPrj = prjConfigs.addElement("Project");
            newPrj.addAttribute("projectName", newPrjName);
            newPrj.addAttribute("projectFilePath", newPrjXml.getPath());
            newPrj.addAttribute("projectFilePath", newPrjXml.getPath());
            newPrj.addAttribute("isActivated", "false");

            XMLWriter configXmlWriter = new XMLWriter(new FileWriter(Context.configureFilePath),
                    OutputFormat.createPrettyPrint());
            configXmlWriter.write(configureDoc);
            configXmlWriter.flush();

            insertDefaultMapping(new FileInputStream(defaultXmlPath), root, newPrjPath);

            List<Element> newDirElementsList = newXmlDoc.getRootElement().element("userMapping").elements();

            for (Element newDirElement : newDirElementsList) {
                File newDir = new File(newDirElement.attributeValue("absolutePath"));
                newDir.mkdirs();
            }



            newXmlWriter.write(newXmlDoc);
            newXmlWriter.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete given file and its sub file recursively.
     * */
    private static boolean delDir(File dir) {
        if (!dir.isDirectory() || dir.list().length == 0) {
            return dir.delete();
        }
        Arrays.stream(dir.listFiles()).forEach(child -> delDir(child));
        return dir.delete();
    }

    /**
     * Remove selected file mappings and its sub files from its project XML file and delete its real file if needed.
     * @param isDeleteFile true to delete real file, false to delete elements in XML only.
     * @param removedList the list with abstract path of files to delete.
     * */
    public static void removeMappingFromXml(String XmlPath, List<String> removedList, boolean isDeleteFile) {
        try {
            Document document = new SAXReader().read(new FileInputStream(XmlPath), "UTF8");
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
            XMLWriter xmlWriter = new XMLWriter(new FileWriter(XmlPath), OutputFormat.createPrettyPrint());
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

    /**
     * Transform nodes of a path into string.
     * @param isFile false to append '/' with string, true otherwise.
     * */
    public static String path2String(TreeNode[] pathNodes, boolean isFile) {
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
        if(!isFile)
            path.append("/");

        return path.toString();
    }

    /**
     * Cancel the closing project in the configure XML.
     * @param prjToClose name of the project to close.
     * */
    public static void closeProject(String prjToClose) {
        try {
            Document document = new SAXReader().read(new FileInputStream(Context.configureFilePath));
            List<Element> projectElementList = document.getRootElement().elements();
            for (Element projectElement: projectElementList) {
                if(projectElement.attributeValue("projectName").equals(prjToClose)) {
                    document.getRootElement().remove(projectElement);
                    Context.getContext().getOpenProjects().remove(prjToClose);
                }
            }

            XMLWriter xmlWriter = new XMLWriter(new FileWriter(Context.configureFilePath),
                    OutputFormat.createPrettyPrint());
            xmlWriter.write(document);
            xmlWriter.flush();
            xmlWriter.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void changeActivatedProject(String prjToActivated) {
        try {
            Document configDoc = new SAXReader().read(new FileInputStream(Context.configureFilePath));
            List<Element> projectElementList = configDoc.getRootElement().elements();
            for (Element projectElement: projectElementList) {
                if(projectElement.attributeValue("projectName").equals(prjToActivated)) {
                    projectElement.addAttribute("isActivated", "true");
                } else{
                    projectElement.addAttribute("isActivated", "false");
                }
            }

            XMLWriter xmlWriter = new XMLWriter(new FileWriter(Context.configureFilePath),
                    OutputFormat.createPrettyPrint());
            xmlWriter.write(configDoc);
            xmlWriter.flush();
            xmlWriter.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
