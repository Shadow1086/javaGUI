package Homework.StudentManage.UI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
/**
 * ClassName: StudentManagerFrame
 * Description: 
 * 
 * 主窗口
 * 
 * {@code @Author} Liang-ht
 * {@code @Create} 2025-11-11 21:03:01
 */
public class StudentManagerFrame {
    JFrame frame = new JFrame("学生管理系统");
    
    //声明菜单相关组件
    JMenuBar menuBar = new JMenuBar();

    JMenu file = new JMenu("文件");
    JMenuItem save = new JMenuItem(new AbstractAction("导出"){
        @Override
        public void actionPerformed(ActionEvent e){
            JFileChooser fileChooser = new JFileChooser(".");
            fileChooser.showSaveDialog(frame);
            File file = fileChooser.getSelectedFile();
            try {
                //TODO:handle the file
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

    });
    JMenuItem  open = new JMenuItem(new AbstractAction("导入") {
        @Override 
        public void actionPerformed(ActionEvent e){
            JFileChooser fileChooser = new JFileChooser(".");
            fileChooser.showOpenDialog(frame);
            File file = fileChooser.getSelectedFile();
            try {
                //TODO: handle the file
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    });



    //声明右键
    ButtonGroup popupButtonGroup = new ButtonGroup();
    JRadioButtonMenuItem delete = new JRadioButtonMenuItem("删除所选行");
    JRadioButtonMenuItem change =  new JRadioButtonMenuItem("修改数据");






    
    //组装组件
    public void init(){
        //组装菜单栏
        menuBar.add(file);

        file.add(save);
        file.add(open);

        frame.setJMenuBar(menuBar);
        //组装右键
        popupButtonGroup.add(change);
        popupButtonGroup.add(delete);



        frame.setPreferredSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
