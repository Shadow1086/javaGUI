package homework.studentManager.studentService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import homework.studentManager.studentUtil.ValidationUtil;
import homework.studentManager.studentUtil.ValidationUtil.ValidationResult;
import homework.studentManager.model.Student;
import homework.studentManager.studentDao.StudentDao;

public class StudentServiceImpl implements StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
    // 作为属性，工厂模式
    private final StudentDao dao;

    public StudentServiceImpl(StudentDao dao) {
        this.dao = dao;
    }

    /**
     * 检验学生信息并添加学生
     */
    @Override
    public boolean addStudent(Student stu) {
        ValidationResult result = ValidationUtil.stuInfoIsValidate(stu);
        if (result.isValid()) {
            boolean success = dao.addStudent(stu);
            return success;
        } else {
            logger.error(result.getMessage());
            return false;
        }
    }

    /**
     * 更新学生信息
     */
    public boolean updateStuInfo(Student stu) {
        ValidationResult result = ValidationUtil.stuInfoIsValidate(stu);
        if (result.isValid()) {
            boolean success = dao.updateStuInfo(stu);
            return success;
        } else {
            logger.error(result.getMessage());
            return false;
        }
    }

    /**
     * 删除学生信息
     */
    @Override
    public boolean deleteStudent(Student stu) {
        return dao.deleteStudent(stu);
    }

    /**
     * 根据学号查找学生信息
     * 
     * @param id : 要查找的目标学生学号
     */
    @Override
    public List<Student> findById(String id) {
        return dao.findById(id);
    }

    /**
     * 根据学生姓名查找学生信息
     * 
     * @param name : 要查找的目标学生姓名
     */
    @Override
    public List<Student> findByName(String name) {
        return dao.findByName(name);
    }

    /**
     * 查找所有学生信息
     */
    @Override
    public List<Student> findAll() {
        return dao.findAll();
    }


}
