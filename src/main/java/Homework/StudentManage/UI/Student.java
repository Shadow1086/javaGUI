package Homework.StudentManage.UI;
import java.text.SimpleDateFormat;

/**
 * 类名: Student
 * 创建时间: 2025/11/19 13:55
 * 项目描述:
 *
 * 创建学生实例，便于管理
 *
 * @author htLiang
 */
public class Student {
    private String name;
    private String stuId;
    private String sex;
    private int age;
    private SimpleDateFormat date;

    public Student(String name, String stuId, String sex, int age) {
        this.name = name;
        this.stuId = stuId;
        this.sex = sex;
        this.age = age;
        setDate();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public SimpleDateFormat getDate() {
        return date;
    }

    public void setDate() {
        this.date = new SimpleDateFormat("yyyyMMMd");
    }
}
