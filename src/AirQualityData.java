import com.fasterxml.jackson.annotation.JsonProperty;

public class AirQualityData {
    @JsonProperty("날짜")
    private String Date;
    @JsonProperty("측정소명")
    private String MeasureStation;
    @JsonProperty("측정소코드")
    private String MeasureStationCode;
    @JsonProperty("PM10")
    private int PM10;
    @JsonProperty("PM2.5")
    private int PM2_5;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getMeasureStation() {
        return MeasureStation;
    }

    public void setMeasureStation(String measureStation) {
        MeasureStation = measureStation;
    }

    public String getMeasureStationCode() {
        return MeasureStationCode;
    }

    public void setMeasureStationCode(String measureStationCode) {
        MeasureStationCode = measureStationCode;
    }

    public int getPM10() {
        return PM10;
    }

    public void setPM10(int PM10) {
        this.PM10 = PM10;
    }

    public int getPM2_5() {
        return PM2_5;
    }

    public void setPM2_5(int PM2_5) {
        this.PM2_5 = PM2_5;
    }
}
