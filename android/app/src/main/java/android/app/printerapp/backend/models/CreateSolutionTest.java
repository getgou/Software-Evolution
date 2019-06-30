package android.app.printerapp.backend.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by eminahromic on 2017-12-12.
 */

public class CreateSolutionTest {

    @SerializedName("qrcode")
    @Expose
    private String qrcode;
    @SerializedName("temperature")
    @Expose
    private String temperature;
    @SerializedName("timeevent")
    @Expose
    private String timeEvent;
    @SerializedName("comment")
    @Expose
    private String comment;

    public String getQrcode() { return qrcode; }

    public void setQrcode(String qrCode) { this.qrcode = qrCode; }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getTimeEvent() {
        return timeEvent;
    }

    public void setTimeEvent(String timeEvent) {
        this.timeEvent = timeEvent;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
