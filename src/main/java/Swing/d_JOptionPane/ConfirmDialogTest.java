package Swing.d_JOptionPane;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class ConfirmDialogTest {
    JFrame frame = new JFrame("确认对话框的测试");

    JTextArea textArea = new JTextArea(6,30);

    JButton button = new JButton(new AbstractAction("点击此按钮以弹出对话框") {
        @Override
        public void actionPerformed(ActionEvent e){
            //弹出确认对话框
            String text = textArea.getText();
            textArea.append("\n");
            // int result = JOptionPane.showConfirmDialog(frame, text,"确认对话框",JOptionPane.DEFAULT_OPTION);
            // int result = JOptionPane.showConfirmDialog(frame, text,"确认对话框",JOptionPane.YES_NO_OPTION);
            int result = JOptionPane.showConfirmDialog(frame, text,"确认对话框",JOptionPane.YES_NO_CANCEL_OPTION);


            if(result == JOptionPane.YES_OPTION){
                textArea.append("用户点击了 是 按钮\n");
            }else if(result == JOptionPane.NO_OPTION){
                textArea.append("用户点击了 否 按钮\n");
            }else if(result == JOptionPane.OK_OPTION){
                textArea.append("用户点击了确认按钮\n");
            }else if(result == JOptionPane.CANCEL_OPTION){
                textArea.append("用户点击了取消按钮\n");
            }
        }
    });


    public void init(){

        frame.add(textArea);
        frame.add(button,BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new ConfirmDialogTest().init();
    }
}
