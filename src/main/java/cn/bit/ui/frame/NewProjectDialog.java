package cn.bit.ui.frame;

import com.github.cjwizard.*;
import com.github.cjwizard.pagetemplates.TitledPageTemplate;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * Created by KlousesSun on 2017/3/31.
 */
public class NewProjectDialog extends JDialog {

    private WizardSettings settings;
    private boolean finished = false;

    public boolean isFinished() {
        return finished;
    }

    public WizardSettings getSettings() {
        return settings;
    }

    public void setSettings(WizardSettings settings) {
        this.settings = settings;
    }

    public NewProjectDialog() {

        setLocationRelativeTo(null);
        setResizable(false);
        setModalityType(ModalityType.APPLICATION_MODAL);

        final WizardContainer wc =
                new WizardContainer(new PageFactory(),
                        new TitledPageTemplate(),
                        new StackWizardSettings());

        wc.addWizardListener(new WizardListener() {

            @Override
            public void onPageChanged(WizardPage newPage, List<WizardPage> path) {
                NewProjectDialog.this.setTitle(newPage.getDescription());
                setSize(newPage.getPreferredSize());
            }

            @Override
            public void onFinished(List<WizardPage> path, WizardSettings settings) {
                setSettings(settings);
                File prjPath = new File(settings.get("prjPathField").toString());
                File prjFullPath = new File(prjPath + "/" + settings.get("prjNameField").toString());
                if(!prjPath.exists() || !prjPath.canWrite()) {
                    JOptionPane.showMessageDialog(null,
                            "目录不存在或不可写",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                } else if(prjFullPath.exists()) {
                    JOptionPane.showMessageDialog(null,
                            "该目录下已存在同名文件夹",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                finished = true;
                NewProjectDialog.this.dispose();
            }


            @Override
            public void onCanceled(List<WizardPage> path, WizardSettings settings) {
                NewProjectDialog.this.dispose();
            }
        });

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(wc);
        setVisible(true);
    }

    private class PageFactory extends APageFactory {
        @Override
        public WizardPage createPage(List<WizardPage> path, WizardSettings settings) {
            return buildPage(path.size(), settings);
        }

        private WizardPage buildPage(int pageIndex, final WizardSettings settings) {
            switch (pageIndex) {
                case 0:
                    return new NewProjectBasicPage("填写工程名及路径", "工程基本信息");
//                case 1:
//                    return new WizardPage("选择创建方式", "创建方式") {
//                        {
//                            setPreferredSize(new Dimension(470, 230));
//                            JCheckBox createByTemplateCheckBox = new JCheckBox("从模板导入");
//                            createByTemplateCheckBox.setName("createByTemplateCheckBox");
//                            JLabel templatePathLabel = new JLabel("模板路径：");
//                            templatePathLabel.setEnabled(false);
//                            JTextField templatePathField = new JTextField();
//                            templatePathField.setName("templatePathField");
//                            templatePathField.setEnabled(false);
//                            templatePathField.setPreferredSize(new Dimension(280, 20));
//                            JButton templatePathButton = new JButton("...");
//                            templatePathButton.setPreferredSize(new Dimension(30, 20));
//                            templatePathButton.setEnabled(false);
//
//                            createByTemplateCheckBox.addActionListener(e -> {
//                                if (createByTemplateCheckBox.isSelected()) {
//                                    templatePathLabel.setEnabled(true);
//                                    templatePathField.setEnabled(true);
//                                    templatePathButton.setEnabled(true);
//                                } else {
//                                    templatePathLabel.setEnabled(false);
//                                    templatePathField.setEnabled(false);
//                                    templatePathButton.setEnabled(false);
//                                }
//                            });
//                            templatePathButton.addActionListener(e -> {
//                                JFileChooser jFileChooser = new JFileChooser();
//                                jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//                                jFileChooser.setMultiSelectionEnabled(false);
//                                if (jFileChooser.showOpenDialog(null) != JFileChooser.CANCEL_OPTION) {
//                                    templatePathField.setText(jFileChooser.getSelectedFile().getPath());
//                                }
//                            });
//
//                            setLayout(new GridLayout(2, 1));
//                            JPanel jPanel1 = new JPanel();
//                            FlowLayout flowLayout1 = new FlowLayout(FlowLayout.LEFT);
//                            jPanel1.setLayout(flowLayout1);
//                            jPanel1.add(createByTemplateCheckBox);
//
//                            JPanel jPanel2 = new JPanel();
//                            FlowLayout flowLayout2 = new FlowLayout(FlowLayout.CENTER);
//                            flowLayout2.setVgap(5);
//                            jPanel2.setLayout(flowLayout2);
//                            jPanel2.add(templatePathLabel);
//                            jPanel2.add(templatePathField);
//                            jPanel2.add(templatePathButton);
//                            add(jPanel1);
//                            add(jPanel2);
//                            setBorder(new EmptyBorder(5, 5, 15, 5));
//                        }
//                    };
//                case 2:
//                    return new WizardPage("填写工程相关信息", "工程配置") {
//                        {
//
//                            setPreferredSize(new Dimension(480, 450));
//                            setBorder(new EmptyBorder(10, 20, 10, 20));
//                            JTextField[] jTextFields = new JTextField[10];
//                            JLabel[] jLabels = {
//                                    new JLabel("被测软件版本:"),
//                                    new JLabel("芯片厂商:"),
//                                    new JLabel("软件开发单位:"),
//                                    new JLabel("软件开发版本:"),
//                                    new JLabel("所属型号:"),
//                                    new JLabel("软件代号:"),
//                                    new JLabel("被测软件顶层名称:"),
//                                    new JLabel("开发语言:"),
//                                    new JLabel("开发环境:"),
//                                    new JLabel("第三方EDA名称:")
//                            };
//
//                            setLayout(new GridLayout(11, 1));
//
//                            for (int i = 0; i < 10; i++) {
//                                jTextFields[i] = new JTextField();
//                                jTextFields[i].setPreferredSize(new Dimension(300, 20));
//                                JPanel jPanel = new JPanel();
//                                jPanel.setLayout(new BorderLayout());
//                                jPanel.add(jLabels[i], BorderLayout.LINE_START);
//                                JPanel jPanel2 = new JPanel();
//                                jPanel2.setLayout(new FlowLayout());
//                                jPanel2.add(jTextFields[i]);
//                                jPanel.add(jPanel2, BorderLayout.LINE_END);
//                                add(jPanel);
//                            }
//
//                            JButton additionButton = new JButton("+");
//                            additionButton.setSize(20, 20);
//                            JPanel jPanel = new JPanel();
//                            jPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
//                            jPanel.add(additionButton);
//                            add(jPanel);
//
//                        }
//                    };
//                case 3:
//                    return new WizardPage("添加配置文件", "工具配置") {
//                        {
//                            setPreferredSize(new Dimension(580, 230));
//                            setBorder(new EmptyBorder(10, 20, 10, 20));
//                            JTextField[] jTextFields = new JTextField[4];
//                            JLabel[] jLabels = {
//                                    new JLabel("规则检查配置文件:"),
//                                    new JLabel("跨时钟域检查配置文件:"),
//                                    new JLabel("仿真配置文件:"),
//                                    new JLabel("静态检查配置文件:")
//                            };
//                            setLayout(new GridLayout(4, 1));
//
//                            for (int i = 0; i < 4; i++) {
//                                jTextFields[i] = new JTextField();
//                                jTextFields[i].setPreferredSize(new Dimension(300, 20));
//                                jTextFields[i].setName(jLabels[i].getText());
//                                JPanel jPanel = new JPanel();
//                                jPanel.setLayout(new BorderLayout());
//                                jPanel.add(jLabels[i], BorderLayout.LINE_START);
//                                JButton pathButton = new JButton("...");
//                                int finalI = i;
//                                pathButton.addActionListener(e -> {
//                                    JFileChooser jFileChooser = new JFileChooser();
//                                    jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//                                    jFileChooser.setMultiSelectionEnabled(false);
//                                    if (jFileChooser.showOpenDialog(null) != JFileChooser.CANCEL_OPTION) {
//                                        jTextFields[finalI].setText(jFileChooser.getSelectedFile().getPath());
//                                    }
//                                });
//                                JPanel jPanel2 = new JPanel();
//                                jPanel2.setLayout(new FlowLayout());
//                                jPanel2.add(jTextFields[i]);
//                                jPanel2.add(pathButton);
//                                jPanel.add(jPanel2, BorderLayout.LINE_END);
//                                add(jPanel);
//                            }
//                        }
//                    };
//                case 4:
//                    return new WizardPage("添加代码文件", "文件配置") {
//                        {
//                            setPreferredSize(new Dimension(540, 260));
//                            setBorder(new EmptyBorder(10, 20, 10, 20));
//                            JTextField[] jTextFields = new JTextField[5];
//                            JLabel[] jLabels = {
//                                    new JLabel("被测件源码:"),
//                                    new JLabel("芯片库文件:"),
//                                    new JLabel("测试方应用平台:"),
//                                    new JLabel("测试用例:"),
//                                    new JLabel("验证VIP:"),
//                            };
//                            setLayout(new GridLayout(5, 1));
//
//                            for (int i = 0; i < 5; i++) {
//                                jTextFields[i] = new JTextField();
//                                jTextFields[i].setPreferredSize(new Dimension(300, 20));
//                                JPanel jPanel = new JPanel();
//                                jPanel.setLayout(new BorderLayout());
//                                jPanel.add(jLabels[i], BorderLayout.LINE_START);
//                                JButton pathButton = new JButton("...");
//                                int finalI = i;
//                                pathButton.addActionListener(e -> {
//                                    JFileChooser jFileChooser = new JFileChooser();
//                                    jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//                                    jFileChooser.setMultiSelectionEnabled(false);
//                                    if (jFileChooser.showOpenDialog(null) != JFileChooser.CANCEL_OPTION) {
//                                        jTextFields[finalI].setText(jFileChooser.getSelectedFile().getPath());
//                                    }
//                                });
//                                JPanel jPanel2 = new JPanel();
//                                jPanel2.setLayout(new FlowLayout());
//                                jPanel2.add(jTextFields[i]);
//                                jPanel2.add(pathButton);
//                                jPanel.add(jPanel2, BorderLayout.LINE_END);
//                                add(jPanel);
//                            }
//                        }
//                    };
//                case 5:
//                    return new ConfirmPage(settings);

            }
            return null;
        }

        private class ConfirmPage extends WizardPage {

            public ConfirmPage(WizardSettings settings) {
                super("工程信息确认", "工程信息确认");
                setPreferredSize(new Dimension(400, 300));
                setBorder(new EmptyBorder(10, 50, 10, 20));
                setLayout(new GridLayout(7, 1));
                JLabel prjNameLabel = new JLabel("工程名:" + settings.get("prjNameField").toString());
                JLabel prjPathLabel = new JLabel("工程路径:" + settings.get("prjPathField").toString());
                JLabel createByTemplateLabel = new JLabel("通过模板创建:");
                if (((boolean) settings.get("createByTemplateCheckBox")) == true) {
                    createByTemplateLabel.setText(createByTemplateLabel.getText() + "是");
                } else {
                    createByTemplateLabel.setText(createByTemplateLabel.getText() + "否");
                }

                JLabel prjTest1Label = new JLabel("规则检查配置:" + settings.get("规则检查配置文件:").toString());
                JLabel prjTest2Label = new JLabel("跨时钟域检查配置:" + settings.get("跨时钟域检查配置文件:").toString());
                JLabel prjTest3Label = new JLabel("仿真配置:" + settings.get("仿真配置文件:").toString());
                JLabel prjTest4Label = new JLabel("静态检查配置:" + settings.get("静态检查配置文件:").toString());

                add(prjNameLabel);
                add(prjPathLabel);
                add(createByTemplateLabel);
                add(prjTest1Label);
                add(prjTest2Label);
                add(prjTest3Label);
                add(prjTest4Label);
            }

            @Override
            public void rendering(List<WizardPage> path, WizardSettings settings) {
                super.rendering(path, settings);
                setFinishEnabled(true);
                setNextEnabled(false);
            }
        }


    }
}
