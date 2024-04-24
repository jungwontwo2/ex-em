import java.sql.*;
import java.util.Queue;
import java.util.LinkedList;

public class AirQualityAlertSystem {
    private static final int PM10_ALERT = 300;
    private static final int PM10_WATCH = 150;
    private static final int PM25_ALERT = 150;
    private static final int PM25_WATCH = 75;

    // 2시간 이상 지속해야 하므로, 2개의 데이터를 저장할 큐 생성
    private Queue<AirQualityData> dataQueue = new LinkedList<>();
    public void processAirQualityData(AirQualityData data) {
        // 데이터를 큐에 추가
        dataQueue.offer(data);
    }

    // 큐를 검사하여 2시간 이상 지속되는 높은 농도 확인
    public void checkForAlerts() {
        int pm10WatchCount = 0;
        int pm10AlertCount = 0;
        int pm25WatchCount = 0;
        int pm25AlertCount = 0;

        for (AirQualityData data : dataQueue) {
            if(data.getPM10()==0 & data.getPM2_5()==0){
                insertChecktimeIntoDatabase(data.getMeasureStation(),"모든 기계",data.getDate());
            }
            else if(data.getPM10()==0){
                insertChecktimeIntoDatabase(data.getMeasureStation(),"미세먼지",data.getDate());
            }
            else if(data.getPM2_5()==0){
                insertChecktimeIntoDatabase(data.getMeasureStation(),"초미세먼지",data.getDate());
            }


            // PM10 검사
            if (data.getPM10() >= PM10_ALERT) {
                pm10AlertCount++;
                pm10WatchCount++; // 경보 조건이면 주의보 조건도 충족함
            } else if (data.getPM10() >= PM10_WATCH) {
                pm10WatchCount++;
                pm10AlertCount = 0; // 연속성이 깨짐
            } else {
                pm10WatchCount = 0;
                pm10AlertCount = 0;
            }

            // PM2.5 검사
            if (data.getPM2_5() >= PM25_ALERT) {
                pm25AlertCount++;
                pm25WatchCount++; // 경보 조건이면 주의보 조건도 충족함
            } else if (data.getPM2_5() >= PM25_WATCH) {
                pm25WatchCount++;
                pm25AlertCount = 0; // 연속성이 깨짐
            } else {
                pm25WatchCount = 0;
                pm25AlertCount = 0;
            }

            // 주의보 및 경보 발생 조건 검사
            if (pm25AlertCount >= 2) {
                insertAlertIntoDatabase(data.getMeasureStation(),1,data.getDate());
            }
            else if (pm10AlertCount >= 2) {
                insertAlertIntoDatabase(data.getMeasureStation(),2,data.getDate());
            }
            else if (pm25WatchCount >= 2) {
                insertAlertIntoDatabase(data.getMeasureStation(),3,data.getDate());
            }
            else if (pm10WatchCount >= 2) {
                insertAlertIntoDatabase(data.getMeasureStation(),4,data.getDate());
            }
        }
    }

    private void insertAlertIntoDatabase(String station, Integer alertLevel, String alertTime) {
        // 데이터베이스 연결 및 쿼리 실행 코드
        String sql = "INSERT INTO alerts (station, alert_level, alert_time) VALUES (?, ?, ?)";
        String jdbcUrl = DatabaseInitialization.getFullUrl();
        String dbUser = DatabaseInitialization.getUser();
        String dbPassword = DatabaseInitialization.getPassword();
        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, station);
            pstmt.setInt(2, alertLevel);
            Timestamp alertTimestamp = Timestamp.valueOf(alertTime + ":00:00");
            pstmt.setTimestamp(3, alertTimestamp);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertChecktimeIntoDatabase(String station, String machine, String alertTime) {
        // 데이터베이스 연결 및 쿼리 실행 코드
        String sql = "INSERT INTO checktime (station, machine, alert_time) VALUES (?, ?, ?)";
        String jdbcUrl = DatabaseInitialization.getFullUrl();
        String dbUser = DatabaseInitialization.getUser();
        String dbPassword = DatabaseInitialization.getPassword();
        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, station);
            pstmt.setString(2, machine);
            Timestamp alertTimestamp = Timestamp.valueOf(alertTime + ":00:00");
            pstmt.setTimestamp(3, alertTimestamp);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
