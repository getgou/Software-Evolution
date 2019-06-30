
package android.app.printerapp.backend.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BuildFiles {

    @SerializedName("BuildID")
    @Expose
    private Integer buildID;
    @SerializedName("QR_code")
    @Expose
    private String qRCode;
    @SerializedName("MaterialId")
    @Expose
    private Integer materialId;
    @SerializedName("Status")
    @Expose
    private Integer status;
    @SerializedName("Magic")
    @Expose
    private List<Magic> magic = null;
    @SerializedName("Part")
    @Expose
    private List<Part> part = null;

    public Integer getBuildID() {
        return buildID;
    }

    public void setBuildID(Integer buildID) {
        this.buildID = buildID;
    }

    public String getQRCode() {
        return qRCode;
    }

    public void setQRCode(String qRCode) {
        this.qRCode = qRCode;
    }

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Magic> getMagic() {
        return magic;
    }

    public void setMagic(List<Magic> magic) {
        this.magic = magic;
    }

    public List<Part> getPart() {
        return part;
    }

    public void setPart(List<Part> part) {
        this.part = part;
    }

}
