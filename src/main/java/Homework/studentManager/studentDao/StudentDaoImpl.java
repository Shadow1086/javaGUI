package homework.studentManager.studentDao;

import homework.studentManager.view.Student;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import homework.studentManager.studentUtil.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentDaoImpl implements StudentDao {
    private static final Logger logger = LoggerFactory.getLogger(StudentDaoImpl.class);

    /**
     * 添加学生信息
     * 
     * @param stu 添加的学生信息
     */
    @Override
    public boolean addStudent(Student stu) {
        String sql = """
                INSERT INTO student (stu_id,stu_name,stu_sex,stu_age)
                VALUES(?,?,?,?);
                """;
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = ps.getGeneratedKeys()) {
            ps.setString(1, stu.getStuId());
            ps.setString(2, stu.getName());
            ps.setString(3, stu.getSex());
            ps.setInt(4, stu.getAge());
            while (rs.next()) {
                int id = rs.getInt("id");
                stu.setId(id);
            }
            return ps.execute();
        } catch (SQLException e) {
            logger.error("添加学号为{}的学生失败：" + e, stu.getStuId());
        }
        return true;
    }
    /**
     * 更新学生信息
     * @param Student : 更新之后的学生信息
     */
    @Override
    public boolean updateStuInfo(Student stu){
        String sql = """
                UPDATE student 
                SET stu_id = ?,stu_name = ?,stu_sex = ?,stu_age = ?
                WHERE id = ?;
                """;
        try(Connection conn = DBUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, stu.getStuId());
            ps.setString(2,stu.getName());
            ps.setString(3, stu.getSex());
            ps.setInt(4, stu.getAge());
            ps.setInt(5, stu.getId());
            ps.execute();
        }catch(SQLException e){
            logger.error("更新学号为{}学生信息错误："+e,stu.getId());
        }
        return true;
    }


    /**
     * 删除学生
     * 
     * @param stu 所选的要删除的学生
     */
    @Override
    public boolean deleteStudent(Student stu) {
        String sql = """
                DELETE FROM student
                WHEre stu_id = ? OR stu_name = ?;
                """;
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, stu.getStuId());
            ps.setString(2, stu.getName());
            ps.execute();
        } catch (SQLException e) {
            logger.error("删除学号为{}的学生失败：" + e, stu.getStuId());
        }
        return true;
    }

    /**
     * 查找所有学生信息
     */
    @Override
    public List<Student> findAll() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM student";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(translateStudentFromDatabase(rs));
            }
            return list;
        } catch (SQLException e) {
            logger.error("展示所有学生信息出错：" + e);
        }
        return list;
    }

    /**
     * 查找特定学号的学生
     * @param id 要搜索的学生学号
     */
    public List<Student> findById(String id) {
        String sql = """
                SELECT * FROM student
                WHERE stu_id LIKE ?;
                """;
        List<Student> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            ps.setString(1, id);
            while (rs.next()) {
                Student stu = translateStudentFromDatabase(rs);
                list.add(stu);
            }
            return list;
        } catch (SQLException e) {
            logger.error("查找学号为{}的学生失败：" + e, id);
        }
        return list;
    }
    /**
     * 查找特定学号的学生
     * @param name 要搜索的学生姓名
     */
    public List<Student> findByName(String name) {
        String sql = """
                SELECT * FROM student
                WHERE stu_name LIKE ?;
                """;
        List<Student> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            ps.setString(1, name);
            while (rs.next()) {
                Student stu = translateStudentFromDatabase(rs);
                list.add(stu);
            }
            return list;
        } catch (SQLException e) {
            logger.error("查找姓名为{}的学生失败：" + e, name);
        }
        return list;
    }



    /**
     * ORM映射，将数据库中一行数据变成一个对象
     */
    private Student translateStudentFromDatabase(ResultSet rs) throws SQLException {
        return new Student(rs.getString("stu_id"),
                rs.getString("stu_name"), rs.getString("stu_sex"), rs.getInt("stu_age"));
    }
}
