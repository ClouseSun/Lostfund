package cn.bit.ui.Frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by KlousesSun on 2017/3/30.
 */
public class NewProjectNamePath extends JFrame{
    private JPanel mainPannel;
    private JButton nextButton;
    private JButton cancelButton;
    private JTextField prjNameField;
    private JTextField prjPathField;
    private JButton pathButton;
    private JPanel BottomPanel;

    private String prjName;
    private String prjPath;

    public NewProjectNamePath() {
        super("工程基本信息");
        this.setContentPane(mainPannel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 150);
        this.setResizable(false);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        this.setLocation(screenSize.width/2 - this.getWidth()/2, screenSize.height/2 - this.getHeight()/2);

        pathButton.addActionListener(e -> {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setMultiSelectionEnabled(false);
                jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if(jFileChooser.showOpenDialog(null) != JFileChooser.CANCEL_OPTION) {
                    prjPathField.setText(jFileChooser.getSelectedFile().getPath() + "/" + prjNameField.getText() + "/");
                }
        });

        cancelButton.addActionListener(e -> {
            dispose();
        });

        nextButton.addActionListener(e -> {
            if(null != prjNameField.getText()) {
                prjName = prjNameField.getText();
                prjPath = prjPathField.getText();

                dispose();
            }
        });

        }

    public static void main(String[] args) {
        NewProjectNamePath newProjectNamePath = new NewProjectNamePath();
        newProjectNamePath.setVisible(true);
    }


    public String getPrjName() {
        return prjName;
    }

    public String getPrjPath() {
        return prjPath;
    }
}
