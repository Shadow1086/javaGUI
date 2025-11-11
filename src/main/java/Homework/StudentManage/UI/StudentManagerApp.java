package Homework.StudentManage.UI;
import javax.swing.*;
/**
 * ClassName: StudentManagerApp
 * Description: 
 * 
 * 启动类
 * 
 * {@code @Author} Liang-ht
 * {@code @Create} 2025-11-11 21:02:08
 */
public class StudentManagerApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentManagerFrame().init();
        });
    }
}
