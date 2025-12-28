package homework.studentManager.studentDao;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import homework.studentManager.model.Student;
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
        String sqlGetCreateTime = """
                SELECT create_time FROM student
                WHERE id = ?;
                """;
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            ps.setString(1, stu.getStuId());
            ps.setString(2, stu.getName());
            ps.setString(3, stu.getSex());
            ps.setInt(4, stu.getAge());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                while (rs.next()) {
                    int id = rs.getInt(1);
                    stu.setId(id);
                }
            }
            try (PreparedStatement psTime = conn.prepareStatement(sqlGetCreateTime)) {
                psTime.setInt(1, stu.getId());
                try (ResultSet rsQuery = psTime.executeQuery()) {
                    while (rsQuery.next()) {
                        Timestamp ts = rsQuery.getTimestamp("create_time");
                        if (ts != null) {
                            stu.setCreateTime(
                                    ts.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                        }
                    }
                }
            }
            return true;
        } catch (SQLException e) {
            logger.error("添加学号为{}的学生失败：{}", stu.getStuId(), e);
        }
        return false;
    }

    /**
     * 更新学生信息
     * 
     * @param Student : 更新之后的学生信息
     */
    @Override
    public boolean updateStuInfo(Student stu) {
        String sql = """
                UPDATE student
                SET stu_id = ?,stu_name = ?,stu_sex = ?,stu_age = ?
                WHERE id = ?;
                """;
        String sqlQuery = """
                SELECT update_time FROM student
                WHERE id = ?;
                """;
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setString(1, stu.getStuId());
            ps.setString(2, stu.getName());
            ps.setString(3, stu.getSex());
            ps.setInt(4, stu.getAge());
            ps.setInt(5, stu.getId());
            ps.executeUpdate();

            try (PreparedStatement psQuery = conn.prepareStatement(sqlQuery)) {
                psQuery.setInt(1, stu.getId());
                try (ResultSet rsQuery = psQuery.executeQuery()) {
                    while (rsQuery.next()) {
                        Timestamp tsQuery = rsQuery.getTimestamp("update_time");
                        if (tsQuery != null) {
                            stu.setUpdateTime(tsQuery.toLocalDateTime()
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                        }
                    }
                }
            }
            return true;
        } catch (SQLException e) {
            logger.error("更新学号为{}学生信息错误：{}", stu.getId(), e);
        }
        return false;
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
                WHERE id = ?;
                """;
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, stu.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("删除学号为{}的学生失败：{}", stu.getStuId(), e);
            return false;
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
            logger.error("展示所有学生信息出错：{}", e);
        }
        return list;
    }

    /**
     * 查找特定学号的学生
     * 
     * @param id 要搜索的学生学号
     */
    public List<Student> findById(String id) {
        String sql = """
                SELECT * FROM student
                WHERE stu_id LIKE ?;
                """;
        List<Student> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setString(1, "%" + id + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Student stu = translateStudentFromDatabase(rs);
                    list.add(stu);
                }
                return list;
            }

        } catch (SQLException e) {
            logger.error("查找学号为{}的学生失败：{}", id, e);
        }
        return list;
    }

    /**
     * 查找特定学号的学生
     * 
     * @param name 要搜索的学生姓名
     */
    public List<Student> findByName(String name) {
        String sql = """
                SELECT * FROM student
                WHERE stu_name LIKE ? ;
                """;
        List<Student> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Student stu = translateStudentFromDatabase(rs);
                    list.add(stu);
                }
                return list;
            }

        } catch (SQLException e) {
            logger.error("查找姓名为{}的学生失败：{}", name, e);
        }
        return list;
    }

    /**
     * ORM映射，将数据库中一行数据变成一个对象
     */
    private Student translateStudentFromDatabase(ResultSet rs) throws SQLException {
        Student stu = new Student(rs.getString("stu_id"),
                rs.getString("stu_name"), rs.getString("stu_sex"), rs.getInt("stu_age"));
        stu.setId(rs.getInt("id"));
        Timestamp createTimestamp = rs.getTimestamp("create_time");
        if (createTimestamp != null) {
            stu.setCreateTime(createTimestamp.toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        Timestamp updateTimestamp = rs.getTimestamp("update_time");
        if (updateTimestamp != null) {
            stu.setUpdateTime(updateTimestamp.toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        return stu;
    }
}
