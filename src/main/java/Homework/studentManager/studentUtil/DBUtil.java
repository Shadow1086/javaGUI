package homework.studentManager.studentUtil;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
// import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBUtil {
    private static final HikariDataSource dataSource;

    static{
        HikariConfig config = new HikariConfig("/Homework/StudentManager/util/hikari.properties");
        dataSource = new HikariDataSource(config);
    }
    public static Connection getConnection() throws SQLException{
        return dataSource.getConnection();
    }
    public static void close(){
        if(dataSource != null){
            dataSource.close();
        }
    }
}
