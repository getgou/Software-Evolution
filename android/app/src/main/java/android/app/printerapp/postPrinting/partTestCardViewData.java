package android.app.printerapp.postPrinting;

/**
 * Created by eminahromic on 2017-11-23.
 */

public class partTestCardViewData {

    private String temperature;
    private String time;
    private String detailedInfo;
    private int partTestId;


    public partTestCardViewData(String temperature, String time, String detailedInfo, int partTestId) {
        this.temperature = temperature;
        this.time = time;
        this.detailedInfo = detailedInfo;
        this.partTestId = partTestId;
    }

    public partTestCardViewData(String temperature, String time, String detailedInfo) {
        this.temperature = temperature;
        this.time = time;
        this.detailedInfo = detailedInfo;
    }

    public String getTemperature() {
        return temperature;
    }
    public String getTime() {
        return time;
    }
    public String getDetailedInfo() {
        return detailedInfo;
    }
    public int getPartTestId() {
        return partTestId;
    }
}