package homework.studentManager.studentUtil;

import homework.studentManager.view.Student;
import java.util.regex.Pattern;

import com.mysql.cj.xdevapi.Schema.Validation;

/**
 * ClassName: ValidationUtil
 * Description:
 * 
 * 用来判读学生信息是否正确
 * 
 * {@code @Author} Liang-ht
 * {@code @Create} 2025-12-27 13:07:55
 */
public class ValidationUtil {
    private static final Pattern ID_PATTERN = Pattern.compile("\\d{5,}");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[\\u4e00-\\u9fa5a-zA-z\\s]{2,50}$");

    /**
     * 学生学号
     */
    public static boolean idIsValidate(String id) {
        return ID_PATTERN.matcher(id).matches();
    }

    /**
     * 学生姓名
     */
    public static  boolean nameIsValidate(String name) {
        return NAME_PATTERN.matcher(name).matches();
    }

    /**
     * 验证学生信息是否全部合法
     */
    public static ValidationResult stuInfoIsValidate(Student stu) {
        if (!idIsValidate(stu.getStuId())) {
            return ValidationResult.error("学生学号格式错误");
        }
        if (!nameIsValidate(stu.getName())) {
            return ValidationResult.error("学生姓名格式错误");
        }
        if (!(stu.getSex().equals("男") || stu.getSex().equals("女"))) {
            return ValidationResult.error("学生性别输入错误");
        }
        if (!(stu.getAge() > 0 && stu.getAge() <= 120)) {
            return ValidationResult.error("学生年龄错误");
        }
        return ValidationResult.success();
    }

    /**
     * 验证结果类
     */
    public record ValidationResult(boolean valid, String message) {

        public static ValidationResult success() {
            return new ValidationResult(true, null);
        }

        public static ValidationResult error(String message) {
            return new ValidationResult(false, message);
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }
    }
}
