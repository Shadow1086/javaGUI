package homework.studentManager.studentService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import homework.studentManager.view.Student;
import homework.studentManager.studentUtil.ValidationUtil;
import homework.studentManager.studentUtil.ValidationUtil.ValidationResult;
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
            dao.addStudent(stu);
        } else {
            logger.error(result.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 更新学生信息
     */
    public boolean updateStuInfo(Student stu) {
        ValidationResult result = ValidationUtil.stuInfoIsValidate(stu);
        if (result.isValid()) {
            dao.updateStuInfo(stu);
        } else {
            logger.error(result.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 删除学生信息
     */
    @Override
    public boolean deleteStudent(Student stu) {
        dao.deleteStudent(stu);
        return true;
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

    /**
     * 文件的导入
     */
    public int importFile(File file) {
        int count = 0;
        try (FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr)) {
            if (br.readLine() == "学号,姓名,性别,年龄") {
                String line;
                String[] list;
                while ((line = br.readLine()) != null) {
                    list = line.split(",");
                    Student stu = new Student(list[0], list[1], list[2], Integer.parseInt(list[3]));
                    dao.addStudent(stu);
                    count++;
                }
            }
            return count;
        } catch (IOException e) {
            logger.error("导入文件出错：" + e);
        }
        return -1;
    }

    /**
     * 文件的导出
     */
    public boolean exportFile(File file) {
        if (file.getName().endsWith(".csv")) {
            List<Student> list = dao.findAll();
            try (FileWriter fw = new FileWriter(file);
                    BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write("学号,姓名,性别,年龄\n");
                for (Student stu : list) {
                    bw.write(stu.toString()+"\n");
                }
            }catch(IOException e){
                logger.error("导出到文件出错："+e);
                return false;
            }
        }else{
            logger.error("文件格式不正确");
            return false;  
        }
        return true;
    }
}
