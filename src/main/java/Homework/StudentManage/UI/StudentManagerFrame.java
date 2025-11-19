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
    JMenuItem save=new JMenuItem(new AbstractAction("导出"){@Override public void actionPerformed(ActionEvent e){JFileChooser fileChooser=new JFileChooser(".");fileChooser.showSaveDialog(frame);File file=fileChooser.getSelectedFile();try{
    // TODO:handle the file
    }catch(Exception exception){exception.printStackTrace();}}

    });
    JMenuItem open=new JMenuItem(new AbstractAction("导入"){@Override public void actionPerformed(ActionEvent e){JFileChooser fileChooser=new JFileChooser(".");fileChooser.showOpenDialog(frame);File file=fileChooser.getSelectedFile();try{
    // TODO: handle the file
    }catch(Exception exception){exception.printStackTrace();}}});

    // 表头（列名）
    Vector<String> columnNames=new Vector<String>(){{add("编号");add("姓名");add("性别");add("学号");add("年龄");add("创建时间");}};
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
     // 声明右键
    JPopupMenu popupMenu = new JPopupMenu();
    ButtonGroup popupButtonGroup = new ButtonGroup();
    JRadioButtonMenuItem delete = new JRadioButtonMenuItem("删除所选行");
    JRadioButtonMenuItem change = new JRadioButtonMenuItem("修改数据");

//声明底部
    JTextField tfName = new JTextField(10);
    JTextField tfStuId = new JTextField(10);
    JTextField tfSex = new JTextField(10);
    JTextField tfAge = new JTextField(10);

    JButton cencel = new JButton("取消");
    JButton ok = new JButton("确定");
    // 组装组件
    public void init() {
        // 添加数据
        addStudent(tableValue);
        // 组装表格面板
        tableBoxPanel.add(table.getTableHeader(), BorderLayout.NORTH);
        tableBoxPanel.add(scorllPane, BorderLayout.CENTER);

        // 组装菜单栏
        menuBar.add(file);

        file.add(save);
        file.add(open);

        frame.setJMenuBar(menuBar);
        //组装底部
        JPanel bottomPane = new JPanel();
        bottomPane.add(tfName);
        bottomPane.add(tfStuId);
        bottomPane.add(tfSex);
        bottomPane.add(tfAge);
        bottomPane.add(cencel);
        bottomPane.add(ok);
        frame.add(bottomPane,BorderLayout.SOUTH);
        // 组装右键
        popupButtonGroup.add(delete);
        popupButtonGroup.add(change);
        popupMenu.add(change);
        popupMenu.add(delete);
        // 给表格添加右键菜单监听器
            delete.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
            int selectedRow = table.getSelectedRow();
            if(selectedRow != -1){
                dataModal.removeRow(selectedRow);
            }else{
                JOptionPane.showMessageDialog(frame,"请选中要删除的行");
            }
        }
    });
    change.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
            int row = table.getSelectedRow();
            if(row != -1){
                //TODO：补全修改功能
            }
        }
    });
        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger())
                    showMenu(e);
            }

            private void showMenu(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    table.setRowSelectionInterval(row, row);
                }
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });

        frame.add(tableBoxPanel);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
