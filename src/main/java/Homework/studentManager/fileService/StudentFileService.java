package homework.studentManager.fileService;

import homework.studentManager.model.Student;
import homework.studentManager.studentDao.StudentDao;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StudentFileService {
    private static final Logger logger = LoggerFactory.getLogger(StudentFileService.class);
    private final StudentDao dao;

    public StudentFileService(StudentDao dao) {
        this.dao = dao;
    }    
    /**
     * 文件的导入
     */
    public int importFile(File file) {
        int count = 0;
        try (FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr)) {
            if ("学号,姓名,性别,年龄,创建时间,更新时间".equals(br.readLine())) {
                String line;
                String[] list;
                while ((line = br.readLine()) != null) {
                    list = line.split(",");
                    Student stu = new Student(list[0], list[1], list[2], Integer.parseInt(list[3]));

                    stu.setCreateTime(list[4]);
                    stu.setUpdateTime(list[5]);
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
                bw.write("学号,姓名,性别,年龄,创建时间,更新时间\n");
                for (Student stu : list) {
                    bw.write(stu.getStuId() + "," + stu.getName() + "," + stu.getSex() + "," + stu.getAge() + ","
                            + format(stu.getCreateTime()) + "," + format(stu.getUpdateTime()) + "\n");
                }
            } catch (IOException e) {
                logger.error("导出到文件出错：" + e);
                return false;
            }
        } else {
            logger.error("文件格式不正确");
            return false;
        }
        return true;
    }

    /**
     * 将localDateTime转化为字符串
     */
    private static String format(LocalDateTime time) {
        if (time == null) {
            return "";
        }
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
