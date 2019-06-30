
package android.app.printerapp.backend.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Magic {

    @SerializedName("MagicID")
    @Expose
    private Integer magicID;
    @SerializedName("FileName")
    @Expose
    private String fileName;
    @SerializedName("MagicScreenshot")
    @Expose
    private String magicScreenshot;
    @SerializedName("BuildId")
    @Expose
    private Integer buildId;

    public Integer getMagicID() {
        return magicID;
    }

    public void setMagicID(Integer magicID) {
        this.magicID = magicID;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMagicScreenshot() {
        return magicScreenshot;
    }

    public void setMagicScreenshot(String magicScreenshot) {
        this.magicScreenshot = magicScreenshot;
    }

    public Integer getBuildId() {
        return buildId;
    }

    public void setBuildId(Integer buildId) {
        this.buildId = buildId;
    }

}
