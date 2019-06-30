package android.app.printerapp.postPrinting;

/**
 * Created by eminahromic on 2017-11-09.
 */

public class cardViewData {

    private String temperature;
    private String time;
    private String detailedInfo;
    private String hasGas;

    public cardViewData(String temperature, String time, String hasGas, String detailedInfo) {
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
}
