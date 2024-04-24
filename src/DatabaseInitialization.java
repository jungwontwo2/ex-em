import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitialization {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DATABASE_NAME = "AirQualityAlert";
    private static final String FULL_URL = URL + DATABASE_NAME;
    private static final String USER = "exemAirQuality";
    private static final String PASSWORD = "exemAirQualityPassword";

    public static String getFullUrl(){
        return FULL_URL;
    }
    public static String getUser() {
        return USER;
    }
    public static String getPassword() {
        return PASSWORD;
    }
    public static void createUser(){
        try {
            // 데이터베이스 연결
            Connection connection = DriverManager.getConnection(URL, "root", "root");

            // SQL 명령을 실행하기 위한 Statement 객체 생성
            Statement statement = connection.createStatement();

            // 새 사용자 생성 SQL 명령
            String sqlCreateUser = "CREATE USER 'exemAirQuality'@'localhost' IDENTIFIED BY 'exemAirQualityPassword';";
            // 사용자에게 권한 부여 SQL 명령
            String sqlGrantPrivileges = "GRANT ALL PRIVILEGES ON *.* TO 'exemAirQuality'@'localhost';";

            // SQL 명령 실행
            statement.executeUpdate(sqlCreateUser);
            statement.executeUpdate(sqlGrantPrivileges);

            // 권한 적용을 위한 명령
            statement.executeUpdate("FLUSH PRIVILEGES;");

            System.out.println("사용자 생성 및 권한 부여 완료.");

            // 자원 해제
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createDatabase() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            stmt.executeUpdate(sql);
            System.out.println("Database created successfully...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createAlertTable() {
        String sqlCreateTable = "CREATE TABLE IF NOT EXISTS alerts (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "station VARCHAR(255) NOT NULL," +
                "alert_level INTEGER NOT NULL," +
                "alert_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";

        try (Connection conn = DriverManager.getConnection(FULL_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sqlCreateTable);
            System.out.println("Alert Table created successfully...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void createChecktimeTable() {
        String sqlCreateTable = "CREATE TABLE IF NOT EXISTS Checktime (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "station VARCHAR(255) NOT NULL," +
                "machine VARCHAR(255) NOT NULL," +
                "alert_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";

        try (Connection conn = DriverManager.getConnection(FULL_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sqlCreateTable);
            System.out.println("Checktime Table created successfully...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
