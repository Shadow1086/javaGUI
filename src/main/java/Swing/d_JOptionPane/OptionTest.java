package Swing.d_JOptionPane;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.*;
import javax.swing.*;

public class OptionTest {
    JFrame frame = new JFrame("Option中消息框测试");

    JTextArea textarea = new JTextArea(6, 30);

    // 声明按钮
    JButton btn = new JButton(new AbstractAction("点击出现对话框") {
        @Override
        public void actionPerformed(ActionEvent event) {
            // 按钮点击后，弹出一个消息对话框，并显示文本域中输入的内容
            String text = textarea.getText();

            // 第四个参数：int messageType 指定消息对话框的类型，错误消息，警告消息，问题消息，解释消息（最基本消息，没有图标）
            // JOptionPane.showMessageDialog(frame, text,"消息对话框",JOptionPane.ERROR_MESSAGE);
            ImageIcon image = new ImageIcon("/Volumes/study/02-java/javaGUI/src/main/resources/对话框图标.png");
            JOptionPane.showMessageDialog(frame, text, "消息对话框", JOptionPane.PLAIN_MESSAGE,
                    new ImageIcon(image.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
        }
    });

    public void init() {
        // 组装视图
        frame.add(textarea);
        frame.add(btn, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        new OptionTest().init();
    }
}
