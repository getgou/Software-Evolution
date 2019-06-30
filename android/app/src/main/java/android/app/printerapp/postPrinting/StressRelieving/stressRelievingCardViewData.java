package android.app.printerapp.postPrinting.StressRelieving;

/**
 * Created by eminahromic on 2017-12-12.
 */

public class stressRelievingCardViewData {

    private String temperature;
    private String time;
    private String detailedInfo;
    private String hasGas;
    private int partTestId;

    public stressRelievingCardViewData(String temperature, String time, String hasGas, String detailedInfo, int partTestId) {
        this.temperature = temperature;
        this.time = time;
        this.detailedInfo = detailedInfo;
        this.hasGas = hasGas;
        this.partTestId = partTestId;
    }


    public stressRelievingCardViewData(String temperature, String time, String hasGas, String detailedInfo) {
        this.temperature = temperature;
        this.time = time;
        this.detailedInfo = detailedInfo;
        this.hasGas = hasGas;
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
    public String getHasGas(){
        return hasGas;
    }
    public int getPartTestId() {
        return partTestId;
    }
}
