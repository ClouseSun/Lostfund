package cn.bit.ui.frame;

import com.github.cjwizard.WizardPage;
import com.github.cjwizard.WizardSettings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by KlousesSun on 2017/4/4.
 */
public class NewProjectBasicPage extends WizardPage {

    public NewProjectBasicPage(String title, String description) {
        super(title, description);

        setPreferredSize(new Dimension(400, 175));
        JTextField prjNameField = new JTextField();
        prjNameField.setName("prjNameField");
        JTextField prjPathField = new JTextField();
        prjPathField.setName("prjPathField");
        prjNameField.setPreferredSize(new Dimension(317, 20));
        prjPathField.setPreferredSize(new Dimension(280, 20));
        JLabel prjNameLabel = new JLabel("项目名:");
        JLabel prjPathLabel = new JLabel("项目路径:");
        JButton prjPathButton = new JButton("...");
        prjPathButton.setPreferredSize(new Dimension(30, 20));
        prjPathButton.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            jFileChooser.setMultiSelectionEnabled(false);
            if (jFileChooser.showOpenDialog(null) != JFileChooser.CANCEL_OPTION) {
                prjPathField.setText(jFileChooser.getSelectedFile().getPath() + "/" + prjNameField.getText() + "/");
            }
        });

        setLayout(new GridLayout(2, 1));
        JPanel jPanel1 = new JPanel();
        FlowLayout flowLayout1 = new FlowLayout(FlowLayout.LEFT);
        flowLayout1.setHgap(12);
        jPanel1.setLayout(flowLayout1);

        jPanel1.add(prjNameLabel);
        jPanel1.add(prjNameField);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanel2.add(prjPathLabel);
        jPanel2.add(prjPathField);
        jPanel2.add(prjPathButton);
        add(jPanel1);
        add(jPanel2);
        setBorder(new EmptyBorder(5, 5, 10, 5));
    }

    @Override
    public void rendering(List<WizardPage> path, WizardSettings settings) {
        super.rendering(path, settings);
        setFinishEnabled(true);
        setNextEnabled(false);
    }
}
