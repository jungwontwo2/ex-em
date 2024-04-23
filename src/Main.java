import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        AirQualityAlertSystem alertSystem = new AirQualityAlertSystem();
        try {
            List<AirQualityData> dataList = objectMapper.readValue(new File("2023년3월_서울시_미세먼지.json"), new TypeReference<List<AirQualityData>>(){});

            // 데이터 리스트 사용
            for (AirQualityData data : dataList) {
//                System.out.println(data.getDate() + ", PM10: " + data.getPM10() + ", PM2.5: " + data.getPM2_5());
                alertSystem.processAirQualityData(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}