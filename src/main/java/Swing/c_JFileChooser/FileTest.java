package Swing.c_JFileChooser;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * ClassName: FileTest
 * Description:
 * 
 * Swing中JFileSelect的学习
 * 
 * {@code @Author} Liang-ht
 * {@code @Create} 2025-11-11 18:35:16
 */
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
