package cn.bit.file;

import cn.bit.Context;
import cn.bit.exec.TestMakefile;
import cn.bit.model.FileNodeEntity;
import cn.bit.ui.component.JXVersionTreeTable;
import cn.bit.ui.frame.Main;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import javax.print.Doc;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

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

            } catch (IOException | DocumentException e) {
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

        } catch (IOException | DocumentException e) {
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
    public static void createNewProjectXmlAndFiles(String newPrjName, String defaultXmlPath, String newPrjPath) {
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
            new TestMakefile.Builder(newPrjPath + Context.DEFAULT_MAKEFILE_PATH).build();
            newXmlWriter.write(newXmlDoc);
            newXmlWriter.close();

        } catch (DocumentException | IOException e) {
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
        Arrays.stream(dir.listFiles()).forEach(FileMappingUtils::delDir);
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
            Element userMappingElement = document.getRootElement().element("userMapping");
            List<Element> userMappings = userMappingElement.elements();

            removedList.forEach(abstractPath -> {
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
        } catch (IOException | DocumentException e) {
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

            projectElementList.
                    stream().
                    filter(projectElement -> projectElement.attributeValue("projectName").equals(prjToClose)).
                    forEach(projectElement -> {
                        document.getRootElement().remove(projectElement);
                        Context.getContext().getOpenProjects().remove(prjToClose);
                    });

            XMLWriter xmlWriter = new XMLWriter(new FileWriter(Context.configureFilePath),
                    OutputFormat.createPrettyPrint());
            xmlWriter.write(document);
            xmlWriter.flush();
            xmlWriter.close();

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void setActivatedProject(String prjToActivated, Main mainFrame) {
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

            mainFrame.getVersionTabbedPane().removeAll();
            Context.getContext().getActiveProject().getExecModels().entrySet().forEach(verModel -> {
                JPanel newVerPanel = new JPanel();
                newVerPanel.setLayout(new BorderLayout());
                JXVersionTreeTable jxVersionTreeTable = new JXVersionTreeTable(verModel.getValue(), mainFrame);
                newVerPanel.add(jxVersionTreeTable);
                mainFrame.getVersionTabbedPane().addTab(verModel.getKey(), newVerPanel);
            });

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void renameFile(String newNameString,
                                  String fileAbstractPath,
                                  String xmlPath,
                                  boolean isRenameFile) {
        try {
            Document projectDoc = new SAXReader().read(new FileInputStream(xmlPath));
            List<Element> userMappings = projectDoc.getRootElement().element("userMapping").elements();

            boolean isFile = !fileAbstractPath.endsWith("/");
            String fileAbsolutePath = null;
            String newFileAbstractPath = null;
            String newFileAbsolutePath = null;

            if(isFile) {
                newFileAbstractPath = fileAbstractPath.substring(0, fileAbstractPath.lastIndexOf('/'))
                        + "/" + newNameString;
            } else {
                int index = fileAbstractPath.substring(0, fileAbstractPath.length() - 1).lastIndexOf('/');
                if(index != -1) {
                    newFileAbstractPath = fileAbstractPath.
                            substring(0, index) + "/" + newNameString + "/";
                } else {
                    newFileAbstractPath = newNameString + "/";
                }
            }

            for (Element mapping: userMappings) {
                if(mapping.attributeValue("abstractPath").equals(fileAbstractPath)) {
                    fileAbsolutePath = mapping.attributeValue("absolutePath");
                }
            }

            if(isFile) {
                newFileAbsolutePath = fileAbsolutePath.substring(0, fileAbsolutePath.lastIndexOf('/'))
                        + "/" + newNameString;
            } else {
                int index = fileAbsolutePath.substring(0, fileAbsolutePath.length() - 1).lastIndexOf('/');
                if(index != -1) {
                    newFileAbsolutePath = fileAbsolutePath.
                            substring(0, index) + "/" + newNameString + "/";
                } else {
                    newFileAbsolutePath = newNameString + "/";
                }
            }

            for (Element mapping: userMappings) {
                if(mapping.attributeValue("abstractPath").startsWith(fileAbstractPath)) {
                    String oldAbstractPath = mapping.attributeValue("abstractPath");
                    mapping.addAttribute("abstractPath",
                            oldAbstractPath.replaceFirst(fileAbstractPath, newFileAbstractPath));
                }
                if(isRenameFile && mapping.attributeValue("absolutePath").startsWith(fileAbsolutePath)) {
                    String oldAbsolutePath = mapping.attributeValue("absolutePath");
                    mapping.addAttribute("absolutePath",
                            oldAbsolutePath.replaceFirst(fileAbsolutePath, newFileAbsolutePath));
                    File oldFile = new File(fileAbsolutePath);
                    File newFile = new File(newFileAbsolutePath);
                    oldFile.renameTo(newFile);
                }
            }

            XMLWriter xmlWriter = new XMLWriter(new FileWriter(xmlPath), OutputFormat.createPrettyPrint());
            xmlWriter.write(projectDoc);
            xmlWriter.flush();
            xmlWriter.close();

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, String> addNewVerToXml(String prjXmlPath, int verIndex) throws IOException, DocumentException {
        Document prjDoc = new SAXReader().read(new FileInputStream(prjXmlPath));
        String prjPath = new File(prjXmlPath).getParent();
        List<Element> userMappings = prjDoc.getRootElement().element("userMapping").elements();
        Map<String, String> newVerMap = new HashMap<>();

        for(Element element : userMappings) {
            if(element.attributeValue("absolutePath").startsWith(prjPath + "/src/ver_" + String.valueOf(verIndex - 1))) {
                Element newVerEle = element.createCopy();
                String abstractPath = newVerEle.attributeValue("abstractPath");
                String absolutePath = newVerEle.attributeValue("absolutePath");
                newVerEle.addAttribute("abstractPath", abstractPath.replaceFirst("ver_" + String.valueOf(verIndex - 1),
                            "ver_" + String.valueOf(verIndex)));

                newVerEle.addAttribute("absolutePath", absolutePath.replaceFirst(prjPath + "/src/ver_" + String.valueOf(verIndex - 1),
                        prjPath + "/src/ver_" + String.valueOf(verIndex)));

            prjDoc.getRootElement().element("userMapping").add(newVerEle);
            newVerMap.put(newVerEle.attributeValue("abstractPath"), newVerEle.attributeValue("absolutePath"));

            }
        }

        XMLWriter xmlWriter = new XMLWriter(new FileWriter(prjXmlPath), OutputFormat.createPrettyPrint());
        xmlWriter.write(prjDoc);
        xmlWriter.close();

        return newVerMap;
    }

}
