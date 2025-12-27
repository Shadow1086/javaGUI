package homework.studentManager.studentService;

import java.io.File;
import java.util.List;

import homework.studentManager.model.Student;

public interface StudentService {
    // 添加学生信息
    boolean addStudent(Student stu);

    //更新学生信息
    boolean updateStuInfo(Student stu);
    // 删除学生信息
    boolean deleteStudent(Student stu);

    // 根据学号查找学生
    List<Student> findById(String id);

    // 根据姓名查找学生
    List<Student> findByName(String name);

    // 查找所有学生信息
    List<Student> findAll();

    //文件的导入导出
    int importFile(File file);
    boolean exportFile(File file);
}   
