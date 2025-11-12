package Swing.d_JOptionPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class OptionDialogTest {
    JFrame frame = new JFrame("选择对话框测试");

    JTextArea jta = new JTextArea(6, 30);
    JButton button = new JButton(new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent a) {
            int result = JOptionPane.showOptionDialog(frame, "请选择号码", "选择对话框", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE, null, new String[] { "大", "中", "小" }, "中");
            switch (result) {
                case 0:
                    jta.append("用户选择了大\n");
                    break;
                case 1:
                    jta.append("用户选择了中\n");
                    break;
                case 2:
                    jta.append("用户选择了小\n");
                    break;
            }
        }
    });

    public void init() {
        frame.add(jta);
        frame.add(button, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new OptionDialogTest().init();
    }
}
