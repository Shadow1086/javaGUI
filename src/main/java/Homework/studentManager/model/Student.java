package homework.studentManager.view;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private int id;

    private String stuId;
    private String name;
    private String sex;
    private int age;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    

    public Student() {
    };

    public Student(String stuId, String name, String sex, int age) {
        this.stuId = stuId;
        this.name = name;
        this.sex = sex;
        this.age = age;
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    public void setCreateTime(String createTimeStr){
        this.createTime = LocalDateTime.parse(createTimeStr,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    public void setUpdateTime(String updateTimeStr){
        this.updateTime = LocalDateTime.parse(updateTimeStr,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
