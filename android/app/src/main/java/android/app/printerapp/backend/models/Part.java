
package android.app.printerapp.backend.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Part {

    @SerializedName("PartID")
    @Expose
    private Integer partID;
    @SerializedName("StlFileName")
    @Expose
    private String stlFileName;
    @SerializedName("PrtFileName")
    @Expose
    private String prtFileName;
    @SerializedName("QR_code")
    @Expose
    private String qRCode;
    @SerializedName("BuildId")
    @Expose
    private Integer buildId;
    @SerializedName("MagicID")
    @Expose
    private String magicID;
    @SerializedName("PartTestDetails")
    @Expose
    private List<Object> partTestDetails = null;

    public Integer getPartID() {
        return partID;
    }

    public void setPartID(Integer partID) {
        this.partID = partID;
    }

    public String getStlFileName() {
        return stlFileName;
    }

    public void setStlFileName(String stlFileName) {
        this.stlFileName = stlFileName;
    }

    public String getPrtFileName() {
        return prtFileName;
    }

    public void setPrtFileName(String prtFileName) {
        this.prtFileName = prtFileName;
    }

    public String getQRCode() {
        return qRCode;
    }

    public void setQRCode(String qRCode) {
        this.qRCode = qRCode;
    }

    public Integer getBuildId() {
        return buildId;
    }

    public void setBuildId(Integer buildId) {
        this.buildId = buildId;
    }

    public String getMagicID() {
        return magicID;
    }

    public void setMagicID(String magicID) {
        this.magicID = magicID;
    }

    public List<Object> getPartTestDetails() {
        return partTestDetails;
    }

    public void setPartTestDetails(List<Object> partTestDetails) {
        this.partTestDetails = partTestDetails;
    }

}

