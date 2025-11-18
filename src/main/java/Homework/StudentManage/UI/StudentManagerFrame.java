package Homework.StudentManage.UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Vector;

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

    // 声明菜单相关组件
    JMenuBar menuBar = new JMenuBar();

    JMenu file = new JMenu("文件");
    JMenuItem save = new JMenuItem(new AbstractAction("导出") {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser(".");
            fileChooser.showSaveDialog(frame);
            File file = fileChooser.getSelectedFile();
            try {
                // TODO:handle the file
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

    });
    JMenuItem open = new JMenuItem(new AbstractAction("导入") {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser(".");
            fileChooser.showOpenDialog(frame);
            File file = fileChooser.getSelectedFile();
            try {
                // TODO: handle the file
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    });

    // 声明右键
    ButtonGroup popupButtonGroup = new ButtonGroup();
    JRadioButtonMenuItem delete = new JRadioButtonMenuItem("删除所选行");
    JRadioButtonMenuItem change = new JRadioButtonMenuItem("修改数据");

    // 表格
    // 表头（列名）
    Vector<String> columnNames = new Vector<String>() {
        {
            add("编号");
            add("姓名");
            add("性别");
            add("学号");
            add("年龄");
            add("创建时间");
        }
    };
    // 创建表格模型
    DefaultTableModel dataModal = new DefaultTableModel(columnNames, 0);
    // 创建JTable表格组件
    JTable table = new JTable(dataModal);
    // 创建带滚动条的面板，并将表格添加到带有滚动条的面板中
    JScrollPane scorllPane = new JScrollPane(table);
    // 将表头添加到面板中（布局的上方）
    // 创建一个容器实例
    JPanel tableBoxPanel = new JPanel(new BorderLayout());

    // 创建数据的表格
    String[][] tableValue = {
            { "01", "ceshi", "nv", "28390e", "28", "asljkdf" }
    };

    // 定义一个方法来添加学生数据
    public void addStudent(String[][] tableValue) {
        for (String[] arr : tableValue) {
            dataModal.addRow(arr);
        }

    }

    // 组装组件
    public void init() {
        // 组装表格面板
        tableBoxPanel.add(table.getTableHeader(), BorderLayout.NORTH);
        tableBoxPanel.add(scorllPane, BorderLayout.CENTER);

        // 组装菜单栏
        menuBar.add(file);

        file.add(save);
        file.add(open);

        frame.setJMenuBar(menuBar);
        // 组装右键
        popupButtonGroup.add(change);
        popupButtonGroup.add(delete);


        frame.add(tableBoxPanel);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
