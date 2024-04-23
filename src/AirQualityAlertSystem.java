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

        // 큐를 검사하여 2시간 이상 지속되는 높은 농도 확인
        checkForAlerts();
    }

    private void checkForAlerts() {
        int pm10WatchCount = 0;
        int pm10AlertCount = 0;
        int pm25WatchCount = 0;
        int pm25AlertCount = 0;

        for (AirQualityData data : dataQueue) {
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
                System.out.println("초미세먼지 PM2.5 경보 발령");
            }
            if (pm10AlertCount >= 2) {
                System.out.println("미세먼지 PM10 경보 발령");
            }
            if (pm25WatchCount >= 2) {
                System.out.println("초미세먼지 PM2.5 주의보 발령");
            }
            if (pm10WatchCount >= 2) {
                System.out.println("미세먼지 PM10 주의보 발령");
            }




//            // 큐 관리: 2시간 이상 지난 데이터는 제거
//            if (dataQueue.size() > 2) {
//                dataQueue.poll();
//            }
        }
    }
}
