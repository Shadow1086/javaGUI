package homework.studentManager.factory;

/**
 * ClassName: AppFactory
 * Description: 
 * 
 * 工厂类
 * {@code @Author} Liang-ht
 * {@code @Create} 2025-12-27 13:33:23
 */

import homework.studentManager.studentDao.StudentDao;
import homework.studentManager.studentDao.StudentDaoImpl;
import homework.studentManager.studentService.StudentService;
import homework.studentManager.studentService.StudentServiceImpl;

public class AppFactory {
    public static StudentDao createStudentDao() {
        return new StudentDaoImpl();
    }

    public static StudentService createStudetnService() {
        return new StudentServiceImpl(createStudentDao());
    }
}
