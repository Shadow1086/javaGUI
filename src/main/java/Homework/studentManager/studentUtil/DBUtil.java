package homework.studentManager.studentUtil;

import java.sql.Connection;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariConfig;
// import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;
public class DBUtil {
    private static final HikariDataSource dataSource;

    static{
        Dotenv dotenv = Dotenv.load();
        HikariConfig config = new HikariConfig("/Homework/StudentManager/util/hikari.properties");
        config.setJdbcUrl(dotenv.get("DB_URL"));
        config.setUsername(dotenv.get("DB_USER"));
        config.setPassword(dotenv.get("DB_PASS"));
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
