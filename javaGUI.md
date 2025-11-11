# 1.swing的特征
swing组件使用MVC（Model-View-Controller,即模型，视图，控制器设计模式）
模型：用于维护组件的各种状态
视图：是组件可视化的表现
控制器：用于控制对于各种事件，组件作出响应

#  基本组件
```java
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class SwingComponentDemo {
    JFrame frame = new JFrame("测试Swing基本组件");
    // 声明菜单相关的组件
    JMenuBar menuBar = new JMenuBar();

    JMenu fileMenue = new JMenu("文件");
    JMenu editMenue = new JMenu("编辑");

    JMenuItem auto = new JMenuItem("自动换行");
    //调整图标的大小
    int hight = 15 ; int width = 15;
    ImageIcon copyIcon = new ImageIcon("/Volumes/study/02-java/javaGUI/src/main/resources/fuzhi-L.png");
    ImageIcon pasteIcon = new ImageIcon("/Volumes/study/02-java/javaGUI/src/main/resources/niantie.png");
    JMenuItem copy = new JMenuItem("复制",new ImageIcon(copyIcon.getImage().getScaledInstance(width, hight, Image.SCALE_SMOOTH)));
    JMenuItem paste = new JMenuItem("粘贴",new ImageIcon(pasteIcon.getImage().getScaledInstance(width, hight, Image.SCALE_SMOOTH)));

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
    ImageIcon okOIcon = new ImageIcon("/Volumes/study/02-java/javaGUI/src/main/resources/确定.png");
    JButton ok = new JButton("确定",new ImageIcon(okOIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));

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
                } catch (Exception ex) {
                    ex.printStackTrace();
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

```

实现效果：
![][image-1]


## 边框
- 特殊的Border：
	- TitleBorder:为其他边框设置标题，需要传入一个其他的Border对象
	- CompoundBorder:用来组合其他的两个边框，创建该类的对象时，要穿入其他两个Border对象，一个作为内边框，一个作为外边框
- 给组件设置边框步骤：
	- 使用BorderFactory或者xxxBorder创建Border的实例对象
	- 调用swing组件中的setBorder(Border b) 方法为组件设置边框

## 文件：JFileChoose
### 1.使用步骤
1. 创建JFileChooser对象：
```java
//指定默认打开的本地磁盘路径
JFileChooser chooser = new JFileChooser();	
```
2. 调用方法进行初始化
```java
//设定默认选中的文件
setSelectedFile(File file)/setSelectedFiles(File[] files);
//设置是否可以多选，默认为单选
setMultiSelectionEnabled(boolean b);
//设置可以选择内容，例如文件，文件夹等，默认只能选择文件
setFileSelectionMode(int mode);
```
3. 打开文件对话框
```
//打开文件加载对话框，并指定父组件
showOpenDialog(Component parent);
//打开文件保存对话框，并指定父组件
showSaveDialog(Component parent);
```
4. 获取用户选择的结果
```
//获取用户选择的一个文件
File getSelectedFile();
//获取用户选择的多个文件
File[] getSelectFiles();
```
### 实例：
```
public class FileTest {
    JFrame frame = new JFrame("JFileSelect测试");

    JMenuBar jmb = new JMenuBar();
    // 创建菜单
    JMenu menu = new JMenu("文件");
    JMenuItem open = new JMenuItem(new AbstractAction("打开") {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 显示一个文件选择器
            JFileChooser fileChooser = new JFileChooser(".");
            fileChooser.showOpenDialog(frame);
            // 获取用户选择的文件
            File file = fileChooser.getSelectedFile();
            // 进行显示
            try {
                image = ImageIO.read(file);
                drawArea.repaint();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }
    });

    JMenuItem save = new JMenuItem(new AbstractAction("另存为") {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 显示一个文件选择器
            JFileChooser fileChooser = new JFileChooser(".");
            //让用户选择保存的文件名
            fileChooser.showSaveDialog(frame);

            //获取用户选择的保存文件路径
            File file = fileChooser.getSelectedFile();

            try {
                ImageIO.write(image,"png",file);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            
        }
    });
    BufferedImage image;

    // 好处：Swing提供的组件都使用了图像缓冲区技术
    private class MyCanvas extends JPanel {
        @Override
        public void paint(Graphics g) {
            g.drawImage(image, 0, 0, null);
        }
    }
	//画板
    MyCanvas drawArea = new MyCanvas();

    public void init() {
        menu.add(open);
        menu.add(save);

        jmb.add(menu);
        frame.setJMenuBar(jmb);

        drawArea.setPreferredSize(new Dimension(740,500));

        frame.add(drawArea);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new FileTest().init();
    }
}
```
## JOptionPane：对话框
### 基本概述
创建对话框
- 方法：
```
//消息对话框，告知用户某事已经发生，用户只能当季确定按钮
showMessageDialog/showInternalMessageDialog
//确认对话框，用户可以选择yes/no
showConfirmDialog/showInternalConfirmDialog
//输入对话框，提示输入某些信息，该方法返回用户输入的字符串
showInputDialog/showInternalInputDialog
//自定义选项对话框，允许自定义选项，
showOptionDialog/showInternalOptionDialog
```
- 重载：
```
showXxxDialog(Component parentComponent,	//当前对话框的父组件
	Object message,			//对话框上显示的信息，可以是字符串组件图片
	String title,			//当前对话框的标题
	int optionType,			
	int messageType,	
	Icon icon,			//当前对话框左上角的图标
	Object[] options,	//自定义下拉列表的选项
	Object initialValue	//自定义选项中的默认选中项
)
/*
- optionType:当前对画卷上显示的按钮类型：DEFAULT_OPTION，YES_ON_OPTION，YES_NO_CANCEL_OPTION
- messageType:当前对话框的类型,ERROR_MESSAGE,INFOMATION_MESSAGE,WARNING_MESSAGE,QUEStiON_MESSAGE,PLAIN_MESSAGE
```

- YES_OPTION= 0_
- NO_OPTION = 1_
- CANCEL_OPTION= 2_
- OK_OPTION = 0

































[image-1]:	https://cdn.jsdelivr.net/gh/Shadow1086/myPicture@master/uPic/Snipaste_2025-11-11_16-59-10.png