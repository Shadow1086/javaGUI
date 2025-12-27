package homework.studentManager.studentDao;
import java.util.List;

import homework.studentManager.model.Student;
public interface StudentDao {
    // 添加学生信息
    boolean addStudent(Student stu);

    //更新学生信息
    boolean updateStuInfo(Student stu);

    //删除学生信息
    boolean deleteStudent(Student stu);

    //查询学生信息
    List<Student> findAll();
    List<Student> findById(String id);  //根据学号查询
    List<Student> findByName(String name);

    //文件的导入导出

}
