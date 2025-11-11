package Swing.Swing01;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * ClassName: SwingComponentDemo
 * Description:
 * 
 * Swing
 * {@code @Author} Liang-ht
 * {@code @Create} 2025-11-11 14:36:32
 */
public class SwingComponentDemo {
    JFrame frame = new JFrame("测试Swing基本组件");
    // 声明菜单相关的组件
    JMenuBar menuBar = new JMenuBar();

    JMenu fileMenue = new JMenu("文件");
    JMenu editMenue = new JMenu("编辑");

    JMenuItem auto = new JMenuItem("自动换行");
    JMenuItem copy = new JMenuItem("复制");
    JMenuItem paste = new JMenuItem("粘贴");

    JMenu formatMenu = new JMenu("格式");
    JMenuItem comment = new JMenuItem("注释");
    JMenuItem unComment = new JMenuItem("取消注释");

    // 声明文本域
    JTextArea ta = new JTextArea(8, 15);

    // 声明列表框
    String[] colors = { "红色", "绿色", "蓝色" };
    JList<String> colorList = new JList<String>(colors);

    // 声明选择相关组件
    // 下拉选择框
    JComboBox<String> colorSelect = new JComboBox<String>();

    // 单选框，false默认不选中，true默认选中
    // 要实现当选效果，需要有一个组，在组中包含两个按钮，就可以实现只能选一个的效果
    ButtonGroup buttonGroup = new ButtonGroup();

    JRadioButton male = new JRadioButton("男", false);
    JRadioButton female = new JRadioButton("女", true);

    // 复选框
    JCheckBox isMarried = new JCheckBox("是否已婚", true);

    // 声明底部
    JTextField tf = new JTextField(40);
    JButton ok = new JButton("确定");

    // 声明右键菜单
    JPopupMenu jPopupMenu = new JPopupMenu();

    // 右键菜单中的选项，注意只能选择一个
    ButtonGroup popupButtonGroup = new ButtonGroup();

    JRadioButtonMenuItem metalItem = new JRadioButtonMenuItem("Metal 风格");
    JRadioButtonMenuItem nimbusItem = new JRadioButtonMenuItem("Nimbus 风格");
    JRadioButtonMenuItem windowsItem = new JRadioButtonMenuItem("Windows 风格", true); // true是默认选中
    JRadioButtonMenuItem windowsOldItem = new JRadioButtonMenuItem("Windows 经典风格");
    JRadioButtonMenuItem motifItem = new JRadioButtonMenuItem("Motif 风格");

    // 初始化界面
    public void init() {
        // 组装视图

        // 组装底部，将文本框和OK按钮组装成一个panel，将panel组装到frame中的南部
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(tf);
        bottomPanel.add(ok);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        // 组装选择相关的组件
        JPanel selectPanel = new JPanel();

        colorSelect.addItem("红色");
        colorSelect.addItem("绿色");
        colorSelect.addItem("蓝色");

        selectPanel.add(colorSelect);
        buttonGroup.add(male);
        buttonGroup.add(female);
        selectPanel.add(male);
        selectPanel.add(female);

        selectPanel.add(isMarried);

        // 组装文本域和选择相关组件
        Box topLeft = Box.createVerticalBox(); // 垂直BOX
        topLeft.add(ta);
        topLeft.add(selectPanel);

        Box top = Box.createHorizontalBox(); // 平行BOX
        top.add(topLeft);
        top.add(colorList);

        frame.add(top); // 将top放在frame的中心区域

        // 组装顶部菜单栏
        // 组装编辑栏
        formatMenu.add(comment);
        formatMenu.add(unComment);

        editMenue.add(auto);
        editMenue.addSeparator(); // 添加分割线
        editMenue.add(copy);
        editMenue.add(paste);
        editMenue.addSeparator();
        editMenue.add(formatMenu);

        menuBar.add(fileMenue);
        menuBar.add(editMenue);

        frame.setJMenuBar(menuBar);
        ;

        // 组装右键菜单
        popupButtonGroup.add(metalItem);
        popupButtonGroup.add(nimbusItem);
        popupButtonGroup.add(windowsItem);
        popupButtonGroup.add(windowsOldItem);
        popupButtonGroup.add(motifItem);

        // 事件
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 当前选择的是哪一个风格
                String actionCommand = e.getActionCommand();
                try {
                    changeFlavor(actionCommand);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        //处理事件
        metalItem.addActionListener(listener);
        nimbusItem.addActionListener(listener);
        windowsItem.addActionListener(listener);
        windowsOldItem.addActionListener(listener);
        motifItem.addActionListener(listener);
//组装右键菜单
        jPopupMenu.add(metalItem);
        jPopupMenu.add(nimbusItem);
        jPopupMenu.add(windowsItem);
        jPopupMenu.add(windowsOldItem);
        jPopupMenu.add(motifItem);

        //不需要监听鼠标事件
        ta.setComponentPopupMenu(jPopupMenu);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //让frame可见
        frame.pack();
        frame.setVisible(true);
    }

    // 将 changeFlavor 方法放入类内部，并实现多种外观切换
    // 注意：部分 LookAndFeel 只在特定平台可用，使用 UIManager.getSystemLookAndFeelClassName()
    // 作为系统默认回退
    private void changeFlavor(String command) throws Exception {

        switch (command) {
            case "Metal 风格":
                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                break;
            case "Nimbus 风格":
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                break;
            case "Windows 风格":
                // 在不同平台上选择系统默认外观
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                break;
            case "Windows 经典风格":
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
                break;
            case "Motif 风格":
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                break;
            default:
                return;
        }
        //刷新组件外观
        SwingUtilities.updateComponentTreeUI(frame.getContentPane());
        SwingUtilities.updateComponentTreeUI(menuBar);
        SwingUtilities.updateComponentTreeUI(jPopupMenu);
    }
}
