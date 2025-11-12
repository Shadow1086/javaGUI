package Swing.d_JOptionPane;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class InputDialogTest {

    JFrame frame = new JFrame();

    JTextArea textArea = new JTextArea(6,30);
    JButton btn = new JButton(new AbstractAction("点击此按钮弹出对话框") {
        @Override
        public void actionPerformed(ActionEvent action){
            //弹出对话框
            String str = JOptionPane.showInputDialog(frame,"请填写信息","输入对话框",JOptionPane.INFORMATION_MESSAGE);
            textArea.append(str);
        }
    });

    public void init() {
        frame.add(textArea);
        frame.add(btn,BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    public static void main(String[] args) {
        new InputDialogTest().init();
    }
}
