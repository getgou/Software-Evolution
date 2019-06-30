package android.app.printerapp.backend.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by eminahromic on 2017-12-12.
 */

public class UpdateHardening {

    @SerializedName("PartTestId")
    @Expose
    private Integer partTestId;
    @SerializedName("temperature")
    @Expose
    private Integer temperature;
    @SerializedName("timeevent")
    @Expose
    private String timeEvent;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("type")
    @Expose
    private Integer type;

    public Integer getPartTestId() {
        return partTestId;
    }

    public void setPartTestId(Integer partTestId) {
        this.partTestId = partTestId;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public String getTimeEvent() {
        return timeEvent;
    }

    public void setTimeEvent(String timeEvent) {
        this.timeEvent = timeEvent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
